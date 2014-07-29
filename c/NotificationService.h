#if !defined(NOS)
#define NOS

struct NotificationServiceDescription{
	char address[30];
	char publisherAdresses[100][30];
};
typedef struct NotificationServiceDescription NotificationServiceDescription;

struct NotificationService{
	void *context;
	void *publisher;
	void *subscriber;
};
typedef struct NotificationService NotificationService;

NotificationService createNotificationService(NotificationServiceDescription);

void up(NotificationService);

#endif