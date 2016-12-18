import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class BruteCollinearPoints
{
    private Node first = null;
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
        
        findSegments(points);
    }
    
    private class Node
    {
        private LineSegment item;
        private Point startPoint;
        private Point endPoint;
        private Node next;
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
                            
                            enqueue(lsPoints[0], lsPoints[3]);
                        }
                    }
                }
            }
        }
    }
    
    // add the item
    private void enqueue(Point p0, Point p1)   
    {
        if (isDuplicate(p0, p1)) return;
        
        Node node = makeNode(p0, p1);
        if (first == null)
            first = node;
        else
        {
            Node oldFirst = first;
            first = node;
            first.next = oldFirst;
        }
        
        n++;
    }
    
    private Node makeNode(Point p0, Point p1)
    {
        LineSegment ls = new LineSegment(p0, p1);
        Node node = new Node();
        node.startPoint = p0;
        node.endPoint = p1;
        node.item = ls;
        node.next = null;
        return node;
    }
    
    private boolean isDuplicate(Point p0, Point p1)
    {
        Node current = first;
        while (current != null)
        {
            if (current.startPoint.compareTo(p0) == 0
                    && current.endPoint.compareTo(p1) == 0)
                return true;
            current = current.next;
        }
        return false;
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
        Node current = first;
        int i = 0;
        while (current != null)
        {
            copy[i++] = current.item;
            current = current.next;
        }
        
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
