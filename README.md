# keycloak-to-rest-event-listener

##### A Keycloak plugin that publishes events to a rest webservice.  

min Keycloak ver 16.3

For example here is the notification of the creation of a new user

```
 {
  "@class" : "com.nhamza.keykloak.event.provider.EventAdminNotificationMqMsg",
  "time" : 1667904225531,
  "realmId" : "d702a427-b2eb-444e-9b17-794d0bd7ea39",
  "authDetails" : {
    "realmId" : "d702a427-b2eb-444e-9b17-794d0bd7ea39",
    "clientId" : "14af8af8-2cb8-4101-aace-aeecac5f87ff",
    "userId" : "06ecb7ee-b62f-4413-b141-bcf8fa941599",
    "ipAddress" : "172.22.0.1"
  },
  "resourceType" : "USER",
  "operationType" : "CREATE",
  "resourcePath" : "users/32b1d12a-f100-4890-8113-161c80bd2adb",
  "representation" : "{\"username\":\"UserOne\",\"enabled\":true,\"emailVerified\":false,\"firstName\":\"User\",\"lastName\":\"One\",\"email\":\"\",\"requiredActions\":[],\"groups\":[]}",
  "resourceTypeAsString" : "USER"
}
```
The payload will be via a REST post call forwoarded to the in configuration defined rest web service. see configuration below.


## USAGE:
1. Downlad from source or build the jar file ``mvn clean install`` :
2. Copy jar into your Keycloak 
    1. Keycloak version 17+ `/opt/keycloak/providers/keycloak-to-rest-event-listener-1.0.jar` 
3. Configure keycloak as described below 
4. Restart the Keycloak server
5. under `Manage > Events > Config > Events Config > Event Listeners`: Enable the Eventlistner in Keycloak UI by adding **keycloak-to-rest** 

#### Configuration :  just configure **ENVIRONMENT VARIABLES**,
 In case no configuration is submitted the following defalut values will be applied
  - KK_TO_TCM_URL: localhost
  - KK_TO_TCM_PORT: 8042
  - KK_TO_TCM_ENDPOINT: /kcUpdate
  - KK_TO_TCM_USERNAME: user
  - KK_TO_TCM_PASSWORD: password



