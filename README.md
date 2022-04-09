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
java ServidorRMI
```

### Executar o cliente
```
rmic Bin1Client "nome_da_imagem.JPG"
```