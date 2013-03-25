package edu.berkeley.cs160.clairetuna.prog3;

/**
 * Minimum Polygon class for Android.
 * pulled from : Jompe71 on anddev.org: http://www.anddev.org/other-coding-problems-f5/using-java-awt-polygon-in-android-t6521.html#wrap
 * 
 */
public class Polygon
{
    // Polygon coodinates.
    private float[] polyY, polyX;
    private String name;
    // Number of sides in the polygon.
    private int polySides;
    private String fullName;
    private float[] center = new float[2];
    /**
     * Default constructor.
     * @param px Polygon y coods.
     * @param py Polygon x coods.
     * @param ps Polygon sides count.
     */
    public Polygon( float[] px, float[] py, int ps, String name, String fullName)
    {
    	//this means it is a station button
        polyX = px;
        polyY = py;
        polySides = ps;
        this.name = name;
        this.fullName = fullName;
        this.center[0] = (px[0] + px[1]+ px[2] + px[3])/4;
        this.center[1] = (py[0] + py[1]+ py[2] + py[3])/4;
    }
    
    
    public Polygon( float[] px, float[] py, int ps)
    {
        polyX = px;
        polyY = py;
        polySides = ps;
    }
    
    public Polygon( float[] px, float[] py, int ps, String abbrev)
    {
        polyX = px;
        polyY = py;
        name=abbrev;
        polySides = ps;
    }
    public String getName(){
    	return this.name;
    }
    public String getFullName(){
    	return this.fullName;
    }
    public float[] getXCoords(){
    	return polyX;
    	
    }
    
    public float[] getYCoords(){
    	return polyY;
    	
    }
    
    public float[] getCenterCoords(){
    	return center;
    }
    /**
     * Checks if the Polygon contains a point.
     * @see "http://alienryderflex.com/polygon/"
     * @param x Point horizontal pos.
     * @param y Point vertical pos.
     * @return Point is in Poly flag.
     */
    
    public String toString(){
    	return this.name;
    }
    public boolean contains( float x, float y )
    {
        boolean oddTransitions = false;
        for( int i = 0, j = polySides -1; i < polySides; j = i++ )
        {
            if( ( polyY[ i ] < y && polyY[ j ] >= y ) || ( polyY[ j ] < y && polyY[ i ] >= y ) )
            {
                if( polyX[ i ] + ( y - polyY[ i ] ) / ( polyY[ j ] - polyY[ i ] ) * ( polyX[ j ] - polyX[ i ] ) < x )
                {
                    oddTransitions = !oddTransitions;          
                }
            }
        }
        return oddTransitions;
    }  
}