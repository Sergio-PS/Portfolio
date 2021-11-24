package libraries;

/*
Esta clase es la que se obtendra del creador de los mapas. Funciona como imput para el programa y define los parametros de las reglas.
Para este caso, se ha creado una array de string con posiciones obligatorias que ha de pasar el usuario obviando que los "_" son plataformas tambien
Se ha creado este programa para calcular todas las rutas posible independientemente del mapa y las condiciones.
Por ejemplo, si hubieran mas tramos que recorrer: (XO) --> (OS) --> ....... simplemente el creador los añadiria a esta clase input.
 */

public class InputMapCreator {

    public static String[] posicionesObligatorias = {"X", "o", "S"};
}
