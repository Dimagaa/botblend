DELETE
FROM messages;
ALTER SEQUENCE  chats_id_seq RESTART WITH 1;

DELETE
FROM chats;
ALTER SEQUENCE  chats_id_seq RESTART WITH 1;
