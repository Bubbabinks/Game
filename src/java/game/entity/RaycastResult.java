package game.entity;

public class RaycastResult {

    public Status status;
    public Coord hitCoord;
    public Coord origin;
    public Coord target;

    public enum Status {
        hit,
        noHit,
        nullCoord,
        worldNotInit,
        divisionByZero;
    }

}
