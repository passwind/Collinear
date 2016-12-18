import edu.princeton.cs.algs4.StdOut;


public class Test
{

    public static void main(String[] args)
    {
        Point p = new Point(143, 141);
        Point q = p;
        StdOut.println(p.slopeTo(q));

        p = new Point(6, 2);
        q = new Point(2, 3);
        Point r = new Point(6, 2);
        double so1 = p.slopeTo(q);
        double so2 = p.slopeTo(r);
        StdOut.println(so1);
        StdOut.println(so2);
        StdOut.println(so1 < so2);
        
        p = new Point(1000, 17000);
        q = new Point(1000, 31000);
        so1 = p.slopeTo(q);
        StdOut.println(so1);
        r = new Point(1000, 29000);
        so2 = p.slopeTo(r);
        StdOut.println(so2);
        StdOut.println(so1 == so2);
    }

}
