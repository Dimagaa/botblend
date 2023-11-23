INSERT INTO chats (id, external_id)
VALUES (1, 1),
       (2, 2);
ALTER SEQUENCE  chats_id_seq RESTART WITH 3;

INSERT INTO messages(id, chat_id, created_at, sender, content, is_deleted)
VALUES (1, 1, '2023-10-10 10:00:00', 'admin', 'First message: Chat 1', false),
       (2, 1, '2023-10-10 11:00:00', 'user', 'Second message: Chat 1', false),
       (3, 1, '2023-10-10 12:00:00', 'assistant', 'Third message: Chat 1', false),
       (4, 2, '2023-10-10 13:00:00', 'admin', 'Forth message: Chat 2', false),
       (5, 2, '2023-10-10 14:00:00', 'user', 'Fifth message: Chat 2', false),
       (6, 2, '2023-10-10 15:00:00', 'system', 'System deleted message', true);
ALTER SEQUENCE  messages_id_seq RESTART WITH 7;
