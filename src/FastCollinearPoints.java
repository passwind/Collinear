import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints
{
    private Node first = null;
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
        Point[] cPoints = new Point[points.length];
        
        for (int i = 0; i < points.length; i++) cPoints[i] = points[i];
        
        for (int i = 0; i < points.length; i++)
        {
            Point basePoint = points[i];
           
            Comparator<Point> slopeOrder = basePoint.slopeOrder();
            Arrays.sort(cPoints, slopeOrder);
            
            int m = 0;
            
            double lastSo = Double.NEGATIVE_INFINITY;
            
            Point startPoint = basePoint;
            Point endPoint = basePoint;
            
            for (int j = 1; j < cPoints.length; j++)
            {
                double so = basePoint.slopeTo(cPoints[j]);
                if (lastSo == so) 
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
                        enqueue(startPoint, endPoint);
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
            
            if (m >= 3)
            {   
                enqueue(startPoint, endPoint);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
