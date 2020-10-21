SELECT *
FROM project
WHERE NOW() BETWEEN from_date AND to_date OR to_date IS NULL;