import boto3
from botocore.client import Config
import os

session = boto3.session.Session()
client = session.client('s3',
    endpoint_url='https://dbidkfnxhagkmpggyziu.supabase.co/storage/v1/s3',
    aws_access_key_id='df4be3cde0587256d232a35a3e1c9eb2',
    aws_secret_access_key='65a23dcef47a5e639aaea0873b943b8654ef6c809d0a70249874a8a016fdac5e',
    config=Config(s3={'addressing_style': 'path'}, signature_version='s3v4'),
    region_name='ap-northeast-1'
)

try:
    response = client.list_buckets()
    print("SUCCESS with .supabase.co: ", response)
except Exception as e:
    print("ERROR with .supabase.co: ", e)

client2 = session.client('s3',
    endpoint_url='https://dbidkfnxhagkmpggyziu.storage.supabase.co/storage/v1/s3',
    aws_access_key_id='df4be3cde0587256d232a35a3e1c9eb2',
    aws_secret_access_key='65a23dcef47a5e639aaea0873b943b8654ef6c809d0a70249874a8a016fdac5e',
    config=Config(s3={'addressing_style': 'path'}, signature_version='s3v4'),
    region_name='ap-northeast-1'
)

try:
    response = client2.list_buckets()
    print("SUCCESS with .storage.supabase.co: ", response)
except Exception as e:
    print("ERROR with .storage.supabase.co: ", e)

