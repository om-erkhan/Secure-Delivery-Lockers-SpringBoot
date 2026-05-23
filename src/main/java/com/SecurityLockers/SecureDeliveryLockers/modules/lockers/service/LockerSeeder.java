package com.SecurityLockers.SecureDeliveryLockers.modules.lockers.service;

import com.SecurityLockers.SecureDeliveryLockers.modules.lockers.model.Locker;
import com.SecurityLockers.SecureDeliveryLockers.modules.lockers.model.LockerSlot;
import com.SecurityLockers.SecureDeliveryLockers.modules.lockers.repository.LockerRepository;
import com.SecurityLockers.SecureDeliveryLockers.modules.lockers.repository.LockerSlotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LockerSeeder implements CommandLineRunner {

    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private LockerSlotRepository lockerSlotRepository;

    @Override
    public void run(String... args) throws Exception {
        if (lockerRepository.count() == 0) {
            log.info("No lockers found in the database. Seeding dummy lockers and slots...");

            List<LockerData> dummyLockers = getDummyLockerData();

            for (LockerData data : dummyLockers) {
                Locker locker = Locker.builder()
                        .location(data.location)
                        .latitude(data.latitude)
                        .longitude(data.longitude)
                        .lockerImage(data.image)
                        .totalSlots(data.slotsCount)
                        .createdAt(Instant.now())
                        .build();

                Locker savedLocker = lockerRepository.save(locker);

                for (int i = 1; i <= data.slotsCount; i++) {
                    LockerSlot.Size size;
                    if (i % 3 == 1) {
                        size = LockerSlot.Size.SM;
                    } else if (i % 3 == 2) {
                        size = LockerSlot.Size.MD;
                    } else {
                        size = LockerSlot.Size.LG;
                    }

                    LockerSlot slot = LockerSlot.builder()
                            .locker(savedLocker)
                            .slotNumber(i)
                            .size(size)
                            .status(LockerSlot.Status.FREE) // All slots are FREE for testing reservation flow
                            .createdAt(Instant.now())
                            .build();

                    lockerSlotRepository.save(slot);
                }
            }

            log.info("Successfully seeded {} lockers with their slots.", dummyLockers.size());
        } else {
            log.info("Lockers already exist in the database. Skipping locker seeding.");
        }
    }

    private List<LockerData> getDummyLockerData() {
        List<LockerData> list = new ArrayList<>();
        list.add(new LockerData(
                "Downtown Shopping Center (Main Entrance)",
                40.712776,
                -74.005974,
                "https://images.unsplash.com/photo-1558191697-d9b8b2209b5a?auto=format&fit=crop&w=600&q=80",
                6
        ));
        list.add(new LockerData(
                "Grand Central Station (East Wing)",
                40.752726,
                -73.977229,
                "https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&w=600&q=80",
                8
        ));
        list.add(new LockerData(
                "Westside Supermarket (Near Parking)",
                40.783060,
                -73.971249,
                "https://images.unsplash.com/photo-1573164713988-8665fc963095?auto=format&fit=crop&w=600&q=80",
                5
        ));
        list.add(new LockerData(
                "University Student Library (Ground Floor)",
                40.729513,
                -73.996460,
                "https://images.unsplash.com/photo-1606787366850-de6330128bfc?auto=format&fit=crop&w=600&q=80",
                10
        ));
        list.add(new LockerData(
                "Silicon Tech Park (Building B)",
                37.774929,
                -122.419416,
                "https://images.unsplash.com/photo-1595246140625-573b715d11dc?auto=format&fit=crop&w=600&q=80",
                12
        ));
        return list;
    }

    private static class LockerData {
        String location;
        double latitude;
        double longitude;
        String image;
        int slotsCount;

        LockerData(String location, double latitude, double longitude, String image, int slotsCount) {
            this.location = location;
            this.latitude = latitude;
            this.longitude = longitude;
            this.image = image;
            this.slotsCount = slotsCount;
        }
    }
}
