# Conversor-RMI
Trabalho de Sistemas Operacionais para a conversão de uma imagem em tons de cinza para uma imagem em preto e branco.

### Compilar todas as classes .java:
```
javac *.java
```
### Gerar stubs
```
rmic Bin1
```
### Executar o servidor
```
java ServidorRMI "rmi://$hostname:$port/Bin"
```

### Executar o cliente
```
rmic Bin1Client "nome_da_imagem.JPG" "$host1" "$host2" "$host3" "$host4"
```

A string host deve ter o formato "rmi://$hostname:$port/Bin".

