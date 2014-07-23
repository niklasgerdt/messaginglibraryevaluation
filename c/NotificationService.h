#if !defined(NOS)
#define NOS

#include <zmq.h>

struct NotificationServiceDescription{
	char address;
	char publisherAdresses;
};
typedef struct NotificationServiceDescription NotificationServiceDescription;


struct NotificationService{
	char address;
	char publisherAdresses;
};
typedef struct NotificationService NotificationService;

NotificationServiceDescription createNotificationServiceDescription();

NotificationService createNotificationService(NotificationServiceDescription);

#endif