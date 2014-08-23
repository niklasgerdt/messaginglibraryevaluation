
struct Pub
{
	void *context;
	void *publisher;
};
typedef struct Pub Pub;

void pub(Pub, char[]);

Pub init(char[]);

void destroy(Pub);