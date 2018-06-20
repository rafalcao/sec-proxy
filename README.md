# Sec Proxy

Proxy HTTPS reverso.

Links para teste:
  - https://ws-one.rfalcao.com:8444/ - (backend: https://ws-one.rfalcao.com:8081)
  - https://ws-two.rfalcao.com:8445/ - (backend: https://ws-two.rfalcao.com:8082)

### Tecnologia

* [Docker] - Utilizado para utilização do `Nginx`.
* [Embedded Jetty] - `Proxy reverso` para os servidores de backend.

### Instalação
##### - Pré requisitos - 

1 - Necessário que o [docker](https://docs.docker.com/install/) esteja instalado.

2 - Necessário que o [maven](https://maven.apache.org/install.html) esteja instalado.

3 - Necessário que o [java (jdk 1.8)](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html) esteja instalado.

##### Instalação Nginx
``` 
$ docker run --name nginx -d -p 8081:8081 -p 8082:8082 -v /var/www/html/sec-proxy/nginx:/etc/nginx -v /var/www/html/sec-proxy/html:/var/www/html/ nginx:latest
```

##### Rodando a aplicação
```
$ cd /PATH_CLONE/sec-proxy

$ mvn clean package
[INFO] BUILD SUCCESS

$ java -jar target/sec-proxy-1.0-jar-with-dependencies.jar
```

### Adicionais
* [Let's encrypt] - Utilizado para a criação dos certificados, foram aplicados nas chaves.


License
----

MIT


**Free Software, Hell Yeah!**

