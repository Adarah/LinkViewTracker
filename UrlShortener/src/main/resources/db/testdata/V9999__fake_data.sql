-- The version 9999 is so that this script is executed after all other migrations
-- in my integration tests
INSERT INTO redirect (alias, target) VALUES
('foo', 'https://stackoverflow.com'),
('bar', 'https://google.com'),
('baz', 'https://reddit.com');

INSERT INTO visitor (ip, redirect_alias) VALUES
('192.168.10.0', 'foo'),
('192.172.10.154', 'foo'),
('192.168.10.0', 'bar');
