 #Installasjonsguide
**OBS!**
Denne filen må endres/fjernes etter clone/fork.

Forbedringer som kan gjøres:
* Lage maven profil for å bygge produksjonsbygg til frontend.

##Pre-requisits
- Git
- Jdk8+ i path
- Maven i path
- docker(for windows)
- node (npm) (>8 og >5)
- Heroku cli

##Oppsett Docker (windows)
- Først sørg for at virtualization er enablet.
    - Gå til Task Manager.
    - Gå til tab performance og sjekk hva det står under virtualization.
    - Dersom den er disablet, så følg guide for å få enablet den: https://amiduos.com/support/knowledge-base/article/enabling-virtualization-in-lenovo-systems .
    - Restart maskinen og gjør samme sjekk igjen i Task Manager for å se at virtualization nå er enablet.
- Installer docker fra https://store.docker.com/editions/community/docker-ce-desktop-windows
- Velg "use linux container"
- Velg Hyper-V
- Etter ferdig installasjon, gå til settings i docker, og velg: "export deamon on tcp://localhost:2375 without TLS"

##Oppsett før CES
* Heroku login på den spesifikke gruppen (eastindia/telstar/oceanic...)
```
heroku login
```

* Opprett to ulike heroku apper
```
heroku create <backend-app-name>
heroku create <frontend-app-name>
```
* Sett app navnene på følgende steder:

_docker-compose.yml_
```
ces-backend:
  image: registry.heroku.com/<backend-app-name>/web
...
ces-frontend:
  image: registry.heroku.com/<frontend-app-name>/web
```
_backend/pom.xml_
```
<repository>registry.heroku.com/<backend-app-name>/web</repository>
```
_frontend/pom.xml_
```
<repository>registry.heroku.com/<frontend-app-name>/web</repository>
```
_frontend/src/main/webapp/src/environments/environment.prod.ts_
```
apiURL: 'https://<backend-app-name>.herokuapp.com/api/'
```

_README.md_  
Oppdatere kommandoer og URL til endepunktene under _Deploy_.

##Bygg
* ```mvn clean install```
    - Kan kjøres i rot-mappen for å bygge backend og frontend, eller i en av dem.
    - Bygger og lager images

Merk, dersom du får feil som dette:
ces-backend     | /opt/ces/execute.sh: line 2: $'\r': command not found
Sørg for å åpne filen _backend/src/main/docker/execute.sh_ og sett line separator til LF (linux stil)
Deretter må du kjøre mvn clean install igjen for å bygge docker image på nytt

##Kjør lokalt
### Docker
* Kjør ```docker-compose up (-d)``` fra rot-mappen
    * _-d_ brukes om du ønsker at app kjøres som deamon prosess.

* Kjør ```docker-compose down ``` for å ta ned igjen docker containerne.

Da kan man se log ved: ```docker log ces-backend (--tail)``` og ```docker log ces-frontend (--tail)```
* Backend API vil være tilgjengelig på _http://localhost:10001_
* Frontend vil være tilgjengelig på port _http://localhost:10000_

**Tips:** Applikasjonene er eksponerte gjennom portene satt i _docker-compose.yml_.

### Uten docker
***Backend***

* Kommenter ut hele dockerfile-maven-plugin fra maven (_backend/pom.xml_) for å hoppe over bygging av docker image  
```
<!--plugin>
	<groupId>com.spotify</groupId>
	<artifactId>dockerfile-maven-plugin</artifactId>
	...
<plugin--!>
```

* Bygg fra _/backend_   
```mvn clean install```

* Kjør _Application.java_ filen fra IDE(husk å sett port til 10001) 
eller naviger til backend/target og kjør  
```java -Dserver.port=10001 -jar backend.jar```.

Api vil være tilgjengelig på _http://localhost:10001/api_.  
Swagger vil være tilgjengelig på _http://localhost:10001_

***Frontend***

* Naviger til _frontend/src/main/webapp_ og kjør ```npm start```.

App vil være tilgjengelig på _http://localhost:3000_

##Deploy (push og kjør hos heroku)
***Bygg med prod flagg***  
Før man pusher frontend til heroku pass på at du har bygget frontend med prod flagg:  
Endre filen _frontend/pom.xml_ under plugin exec-maven-plugin:
```
<configuration>
    <executable>npm</executable>
    <arguments>
        <argument>run</argument>
        <argument>build:prod</argument>
    </arguments>
</configuration>
```  

Dette gjør blant annet at react bruker env.production filen hvor vi har satt riktig url mot backend.

* Login
```
heroku login
heroku container:login
```
* Push image til heroku
```
docker push registry.heroku.com/<backend-app-name>/web
docker push registry.heroku.com/<frontend-app-name>/web
```
* Restart (med det nye imaget)
```
heroku container:release web -a <backend-app-name>
heroku container:release web -a <frontend-app-name>
```

Webapp vil være tilgjengelig på https://<frontend-app-name>.herokuapp.com/  
Swagger vil være tilgjengelig på https://<backend-app-name>.herokuapp.com/  
API vil være tilgjengelig på https://<backend-app-name>.herokuapp.com/api/

***Tips:***
Les logg fra Heroku ```heroku logs --app <app-name> --tail```

## Tips og feilsøking
#### Maven finner ikke dependenciene mine:
Dette kan skje dersom du bruker et eget artifact repository på prosjektet. Sjekk .m2 mappen din og se om du har 
en settings.xml fil som peker på et custom repository (eg. Nexus). Kommenter ut alt i mellom `<settings>` med `<!-- -->`
og re-importer dependencies etterpå. Dobbeltrykk shift og skriv inn "Reimport", da vil du se valget 
"Reimport all Maven projects". Husk å fjerne kommentering etter CES er ferdig :)
 
