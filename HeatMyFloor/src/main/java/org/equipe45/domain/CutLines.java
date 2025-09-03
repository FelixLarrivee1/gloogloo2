package org.domain;

import org.equipe45.domain.Cut.Cut;
import org.equipe45.dto.CutDTO;
import org.equipe45.dto.CutLineDTO;

import java.awt.*;
import java.util.Objects;
import java.util.UUID;

public class CutLine {
    private final UUID id;
    private Point start;
    private Point end;
    private Cut cut;

    public CutLine(Point start, Point end, Cut cut) {
        this.id = UUID.randomUUID();
        this.start = start;
        this.end = end;
        this.cut = cut;
    }

    public CutLine(UUID id, Point start, Point end, Cut cut) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.cut = cut;
    }

    public Point getStart() {
        return this.start;
    }

    public Point getEnd() {
        return this.end;
    }

    public Cut getCut() {
        return this.cut;
    }

    public UUID getId() {
        return this.id;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public void setCut(Cut cut) {
        this.cut = cut;
    }

    public CutLineDTO toDTO() {
        return new CutLineDTO(start, end, (CutDTO) cut.toDTO());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CutLine)) return false;
        CutLine cutLine = (CutLine) o;
        return id.equals(cutLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CutLine{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", cut=" + cut +
                '}';
    }

    public boolean intersects(CutLine other) {
        // Use Line-Line intersection algorithm
        return linesIntersect(
                this.start.x, this.start.y, this.end.x, this.end.y,
                other.start.x, other.start.y, other.end.x, other.end.y
        );
    }

    private boolean linesIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) return false; // Lines are parallel

        int xi = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        int yi = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        // Check if the intersection point is on both segments
        return isBetween(x1, y1, x2, y2, xi, yi) && isBetween(x3, y3, x4, y4, xi, yi);
    }

    private boolean isBetween(int x1, int y1, int x2, int y2, int xi, int yi) {
        return Math.min(x1, x2) <= xi && xi <= Math.max(x1, x2) &&
                Math.min(y1, y2) <= yi && yi <= Math.max(y1, y2);
    }
}
