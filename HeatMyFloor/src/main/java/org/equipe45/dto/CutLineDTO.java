package org.dto;

import org.equipe45.domain.Cut.Cut;
import org.equipe45.domain.CutLine;

import java.util.UUID;

public class CutLineDTO {
    public Point start;
    public Point end;
    public CutDTO cut;

    public CutLineDTO(Point start, Point end, CutDTO cut)
    {
        this.start = start;
        this.end = end;
        this.cut = cut;
    }

    public CutLine toCutLine()
    {
        return new CutLine(start, end, (Cut) cut.toCut());

    }

    


}

