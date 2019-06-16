# WhitelistDiscord (1.14.2 last compiled Spigot)
A Minecraft plugin that allows Whitelisting via Discord

## Compiling

1.) change directories `cd PATH` replace path with your path.

2.) `git clone https://github.com/TheLimeGlass/WhitelistDiscord`

3.) Create a config.properies in the /resources folder, place the following in it:
```
client.token = insert discord application token
discord.channel = 589038675883589643 //Insert your channels ID, look up how to grab a Discord channel's ID with developer options.
```

4.) `gradle build` if you have gradle installed or `./gradlew build` on unix devices, `gradlew build` works for windows.

5.) Enjoy, compiled version will be in /build/lib/
