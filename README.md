# keycloak-to-rest-event-listener

##### A Keycloak plugin that publishes events to a rest webservice.  

min Keycloak ver 16.3

For example here is the notification of the user updated by administrator

```
{
  "@class" : "com.github.aznamier.keycloak.event.provider.EventAdminNotificationMqMsg",
  "time" : 1596951200408,
  "realmId" : "MYREALM",
  "authDetails" : {
    "realmId" : "master",
    "clientId" : "********-****-****-****-**********",
    "userId" : "********-****-****-****-**********",
    "ipAddress" : "192.168.1.1"
  },
  "resourceType" : "USER",
  "operationType" : "UPDATE",
  "resourcePath" : "users/********-****-****-****-**********",
  "representation" : "representation details here....",
  "error" : null,
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
5. under `Manage > Events > Config > Events Config > Event Listeners` :Enable the Eventlistner in Keycloak UI by adding **keycloak-to-rest** 

#### Configuration :  just configure **ENVIRONMENT VARIABLES**,
 In the case no configuration submitted these defalut value will be applied
        KK_TO_TCM_URL: localhost
        KK_TO_TCM_PORT: 8042
        KK_TO_TCM_ENDPOINT: /kcUpdate
        KK_TO_TCM_USERNAME: user
        KK_TO_TCM_PASSWORD: password



