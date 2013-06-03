package ru.mipt.sign.core;

import java.math.BigInteger;

import org.jdom.Element;

public abstract class SObject implements Comparable<SObject>
{
    protected BigInteger id;
    protected Element image;

    public BigInteger getID()
    {
        return id;
    }

    public SObject(BigInteger id) {
        this.id = id;
    }

    public SObject(Element el) {
        this.image = el;
        this.id = new BigInteger(image.getAttribute("id").getValue());
    }

    public abstract Element getXML();

    public abstract String getType();

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SObject other = (SObject) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    @Override
    public int compareTo(SObject sObject)
    {
        return -id.compareTo(sObject.getID());
    }
}
