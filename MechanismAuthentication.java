//============================================================================
//
//The classes and/or objects participating in this Template Method pattern are:
//1. AbstractClass  (AuthenticationMechanism)
//			Defines abstract primitive operations that concrete subclasses
//			define to implement steps of an algorithm implements a template
//			method defining the skeleton of an algorithm. The template method
//			calls primitive operations as well as operations defined in
//			AbstractClass or those of other objects.
//2. ConcreteClass  (LDAPAuth, KerberosAuth, LocalAuth)
//			implements the primitive operations ot carry out subclass-specific
//			steps of the algorithm
//============================================================================

// Selin Doğa Orhan

// Pluggable Authentication Mechanism

//This is the AbstractClass class.

abstract class AuthenticationMechanism {
    protected abstract void prepareAuthenticating();
    protected abstract int getuid(String name);
    protected abstract int setuid(int uid);
    protected abstract int authenticate (String name,String pwd);

    // This is our template method.
    public final int startAuthentication(String _name,String _pwd) {
        prepareAuthenticating();
        return authenticate(_name, _pwd);
    }
}

