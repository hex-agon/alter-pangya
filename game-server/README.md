Sample server `config.toml`

```toml
id = 20202
name = "Black papel"
capacity = 1000
bindAddress = "127.0.0.1"
advertiseAddress = "127.0.0.1"
port = 20202
flags = []
boosts = ["DOUBLE_PANG"]
icon = "CIEN"

serverChannels = [
    { id = 1, name = "Rookies", capacity = 20, restrictions = ["ROOKIES_ONLY"] },
    { id = 2, name = "Free", capacity = 300 }
]

[database]
url = "jdbc:postgresql://localhost/alter_pangya"
username = "alter_pangya"
password = "alter_pangya"

[redis]
url = "redis://localhost"
```