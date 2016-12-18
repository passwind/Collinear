import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class BruteCollinearPoints
{
    private LineSegment[] lineSegments;
    private int n = 0;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points)
    {
        if (points == null) throw new NullPointerException();
        for (Point p : points)
        {
            if (p == null) throw new NullPointerException();
        }
        
        for (int i = 0; i < points.length-1; i++)
            for (int j = i+1; j < points.length; j++)
            {
                Point p1 = points[i];
                Point p2 = points[j];
                if (p1.compareTo(p2) == 0) throw new IllegalArgumentException();
            }
        
        lineSegments = new LineSegment[1];
        
        findSegments(points);
    }
    
    private void findSegments(Point[] points)
    {
        for (int i = 0; i < points.length-3; i++)
        {
            Point p0 = points[i];
            for (int j = i+1; j < points.length-2; j++)
            {
                Point p1 = points[j];
                double s1 = p0.slopeTo(p1);
                
                for (int k = j+1; k < points.length-1; k++)
                {
                    Point p2 = points[k];
                    double s2 = p0.slopeTo(p2);
                    
                    if (s1 != s2) continue;
                    
                    for (int r = k+1; r < points.length; r++)
                    {   
                        Point p3 = points[r];
                        double s3 = p0.slopeTo(p3);
                        
                        if (s1 == s3)
                        {
                            Point[] lsPoints = new Point[]{p0, p1, p2, p3};
                            Arrays.sort(lsPoints);
                            LineSegment ls = new LineSegment(lsPoints[0], lsPoints[3]);
                            enqueue(ls);
                        }
                    }
                }
            }
        }
    }
    
    // add the item
    private void enqueue(LineSegment item)   
    {
        if (item == null) throw new NullPointerException();
        if (isDuplicate(item)) return;
        if (n == lineSegments.length) resize(2 * lineSegments.length);
        lineSegments[n++] = item;
    }
    
    private boolean isDuplicate(LineSegment item)
    {
        for(int i = 0; i < n; i++)
        {
            LineSegment ls = lineSegments[i];
            if (ls.toString().equals(item.toString())) return true;
        }
        
        return false;
    }

    // change resized-array to increase or decrease space of storage
    private void resize(int capacity)
    {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < n; i++)
            copy[i] = lineSegments[i];
            
        lineSegments = copy;
    }
    
    
    // the number of line segments
    public int numberOfSegments()        
    {
        return n;
    }
    
    // the line segments
    public LineSegment[] segments()
    {
        LineSegment[] copy = new LineSegment[n];
        for (int i = 0; i < n; i++)
            copy[i] = lineSegments[i];
        
        return copy;
    }
    
    public static void main(String[] args)
    {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }

}
