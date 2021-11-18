SELECT transfers.*, a.user_id AS from_user_id, b.user_id AS to_user_id,
transfer_statuses.transfer_status_desc AS transfer_status, 
transfer_types.transfer_type_desc AS transfer_type, c.username AS username_from, d.username AS username_to
FROM transfers
JOIN accounts a ON transfers.account_from = a.account_id
JOIN accounts b ON transfers.account_to = b.account_id
JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id
JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id
JOIN users c ON a.user_id = c.user_id
JOIN users d ON b.user_id = d.user_id 
WHERE c.user_id = 1002 OR d.user_id = 1002

INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (2, 2,
        (SELECT account_id from accounts
        WHERE user_id = 1002),
        (SELECT account_id from accounts
        WHERE user_id = 1001),
        10000)
RETURNING transfer_id

   
UPDATE accounts 
SET balance = balance + ?
WHERE user_id = 1002

SELECT balance
FROM accounts
WHERE user_id = 1002

DELETE 