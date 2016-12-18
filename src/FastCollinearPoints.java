import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints
{
    private LineSegment[] lineSegments;
    private int n = 0;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points)
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
        for (int i = 0; i < points.length; i++)
        {
            Point basePoint = points[i];
            Point[] cPoints = makeComparePoints(points, i);
           
            Comparator<Point> slopeOrder = basePoint.slopeOrder();
            Arrays.sort(cPoints, slopeOrder);
            
            int m = 0;
            double lastSo = Double.NEGATIVE_INFINITY;
            Point startPoint = basePoint;
            Point endPoint = basePoint;
            
            for (int j = 0; j < cPoints.length; j++)
            {
                double so = basePoint.slopeTo(cPoints[j]);
                if (j > 0 && lastSo == so) 
                {
                    m++;
                    if (endPoint.compareTo(cPoints[j]) < 0)
                        endPoint = cPoints[j];
                    if (startPoint.compareTo(cPoints[j]) > 0)
                        startPoint = cPoints[j];
                }
                else
                {
                    if (m >= 3)
                    {   
                        LineSegment ls = new LineSegment(startPoint, endPoint);
                        enqueue(ls);
                    }
                    
                    lastSo = so;
                    m = 1;
                    if (basePoint.compareTo(cPoints[j]) > 0)
                    {
                        startPoint = cPoints[j];
                        endPoint = basePoint;
                    }
                    else  
                    {
                        startPoint = basePoint;
                        endPoint = cPoints[j];
                    }
                } 
            }
        }
    }
    
    private Point[] makeComparePoints(Point[] points, int i)
    {
        Point[] nps = new Point[points.length-1];
        for (int j = 0; j < points.length; j++)
        {
            if (j < i) nps[j] = points[j];
            else if (j > i) nps[j-1] = points[j];
        }
        return nps;
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
        for (int i = 0; i < n; i++)
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
