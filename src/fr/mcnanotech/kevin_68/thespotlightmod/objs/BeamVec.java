package fr.mcnanotech.kevin_68.thespotlightmod.objs;

public class BeamVec
{
    private TSMVec3[] vecs;
    private TSMVec3 lenght;

    public BeamVec(TSMVec3[] vecs, TSMVec3 lenght)
    {
        this.vecs = vecs;
        this.lenght = lenght;
    }

    public TSMVec3[] getVecs()
    {
        return this.vecs;
    }

    public TSMVec3 getLenVec()
    {
        return this.lenght;
    }
}