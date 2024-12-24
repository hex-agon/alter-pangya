Sample server `config.toml`

```toml
id = 10103
name = "Login Server 1"
bindAddress = "127.0.0.1"
advertiseAddress = "127.0.0.1"
port = 10103

[database]
url = "jdbc:postgresql://localhost/alter_pangya"
username = "alter_pangya"
password = "alter_pangya"

[redis]
url = "redis://localhost"
```