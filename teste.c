void func (int n, int origem, int auxiliar, int destino) {

    if (n>0) {

       func (n-1,origem,destino,auxiliar);

       printf ("de %d para %d\n", origem, destino);

       func (n-1,auxiliar,origem,destino);

   }
}

void main() {

  int num;

  printf ("Informe o nº de discos no pino 1:");

  scanf ("%d", &num);

  func(num,1,2,3);

}
