/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package errorhandling;

/**
 *
 * @author Christian
 */
public class DatabaseException extends Exception
{

    public DatabaseException(String message)
    {
        super(message);
    }

    public DatabaseException()
    {
        super("Requested item could not be found");
    }
}
