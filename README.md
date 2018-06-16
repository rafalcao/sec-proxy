# Sec Proxy

Proxy HTTPS reverso.

Links para teste:
  - https://ws-one.rfalcao.com/ - (backend: https://ws-one.rfalcao.com:8081)
  - https://ws-two.rfalcao.com/ - (backend: https://ws-two.rfalcao.com:8082)

### Tecnologia

* [Docker] - Utilizado para instalar o `Nginx`.
* [Nginx] - `Proxy reverso` para os servidores de backend.
* [Embedded Tomcat] - Utilizado para a criação da web aplicação em `JAVA`. 

### Instalação
##### - Pré requisitos - 

1 - Necessário que o [docker](https://docs.docker.com/install/) esteja instalado.

2 - Necessário que o [maven](https://maven.apache.org/install.html) esteja instalado.

3 - Necessário que o [java (jdk 1.8)](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html) esteja instalado.

##### Instalação Nginx
``` 
$ docker run --name nginx -d -p 80:80 -p 443:443 -v /PATH_CLONE/sec-proxy/nginx:/etc/nginx nginx:latest
```
Em seguida deve-se configurar os arquivos de acordo com seus hosts locais
- [ws-one.rfalcao.com.conf]
- [ws-two.rfalcao.com.conf]

##### Rodando a aplicação
```
$ cd /PATH_CLONE/

$ mvn package
[INFO] BUILD SUCCESS

$ sh target/bin/webapp
#### SEC PROXY | Init ws-one service...
#### SEC PROXY | Generate connector for ws-one...
#### SEC PROXY | Start ws-one...
#### SEC PROXY | ws-one ON in 8081 !!!
#### SEC PROXY | Init ws-two service...
#### SEC PROXY | Generate connector for ws-two...
#### SEC PROXY | Start ws-two...
#### SEC PROXY | ws-two ON in 8082 !!!
```

### Adicionais
* [Let's encrypt] - Utilizado para a criação dos certificados, foram aplicados nas chaves.


License
----

MIT


**Free Software, Hell Yeah!**

