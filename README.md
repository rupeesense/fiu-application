# FIU Service

Service to act as a Financial Information User


## Responsibility

1. Initiate Consent Request
2. Receive Consent Callback
3. Store Consent records in datastore


## Database 

RDS with database name = fiu_store


## Deployment

kustomize build k8s | k apply -f -
