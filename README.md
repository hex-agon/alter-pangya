# Alter-Pangya

A PangYa GB.852 server emulator

## Finding a client

This emulator only works with the PangYa GB.852 client, if you browse around the pangya communities you'll easily find a download.
In order to run the client it is highly recommended that you use [RugBurn](https://github.com/pangbox/rugburn).

## Running the servers

### Dependencies

* A redis instance
* A postgres instance

## Development

### Setting up a Postgres database
You can run a local postgres database using the reference `Dockerfile` contained in the `database` directory, to quickly create an instance run:
```bash
docker image build -t alter_pangya database/
docker container run -d -p 5432:5432 --name alter_pangya alter_pangya
```

### Applying the migrations
Replace the relevant database parameters with your own values

```bash
mvn -Dflyway.url=jdbc:postgresql://localhost/alter_pangya -Dflyway.user=alter_pangya -Dflyway.password=alter_pangya -Dflyway.locations=filesystem:database/migrations org.flywaydb:flyway-maven-plugin:migrate --non-recursive
```

### Regenerating the JooQ classes (Schema changes)

While at the project root, run the following bash script to re-generate the jooq classes:

```bash
mvn -Djooq.codegen.jdbc.url=jdbc:postgresql://localhost/alter_pangya -Djooq.codegen.jdbc.user=alter_pangya -Djooq.codegen.jdbc.password=alter_pangya org.jooq:jooq-codegen-maven:generate --non-recursive
```

A big thanks to everyone at the [Caddie's Cauldron discord](https://discord.gg/HwDTssf)

#### References:

* [PangYa Tools](https://github.com/pangyatools)
* [Pangbox](https://github.com/pangbox/)
* [SuperSS](https://github.com/Acrisio-Filho/SuperSS-Dev)