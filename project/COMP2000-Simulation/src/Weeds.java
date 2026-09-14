import java.awt.*;


public class Weeds extends Plant{
    int size;

    Weeds(Point position, Window window){
        super(position, window);
        this.position = position;
        this.setBackground(Color.darkGray);
    }

    @Override

    public void spread(){
        Radius radius = new Radius(position, spreadRadius); //Typo in spread, fixed it - Allie

        for(int i = 0; i < spreadNum; i++){ //your r key might be broken, Sri. Might want to take a look at that. Also int i, not int o - Allie
            Point newPoint = radius.getRandomPoint();
            @SuppressWarnings("unused") //size is an int type that you are trying to call as a Window type. I don't know what this does, so for now I'm supressing this. - Allie
            Weeds child = new Weeds(newPoint, window); //See above, remove line 20 when you've fixed this. 
        }
    }

    @Override
    void seedAction() {
        throw new UnsupportedOperationException("Not supported yet."); //Added an exception to prevent it throwing an error - Allie
    }

    @Override
    void seedlingAction() {
        throw new UnsupportedOperationException("Not supported yet."); //See above - Allie
    }

    @Override
    void juvenileAction() {
        throw new UnsupportedOperationException("Not supported yet."); //Ditto - Allie
    }

    @Override
    void adultAction() {
        throw new UnsupportedOperationException("Not supported yet."); //Fourth verse same as the first - Allie
    }

    @Override
    void deadAction() {
        throw new UnsupportedOperationException("Not supported yet."); //You get the idea - Allie
    }

    
}
