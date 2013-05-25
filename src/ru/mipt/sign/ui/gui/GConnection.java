package ru.mipt.sign.ui.gui;

import java.math.BigInteger;

import ru.mipt.sign.connect.Connection;

public class GConnection
{
    private Connection connection;

    public GConnection(Connection connection) {
        super();
        this.connection = connection;
    }
    
    public BigInteger getID()
    {
        return connection.getID();
    }
}
