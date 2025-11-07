// The classes and/or objects participating in this pattern are:
// 1. Facade   (Authentication)
//		- knows which subsystem classes are responsible for a request.
//		- delegates client requests to appropriate subsystem objects.
// 2. Subsystem classes   (LDAPAuth, KerberosAuth, LocalAuth)
//		- implement subsystem functionality.
//		- handle work assigned by the Facade object.
//		- have no knowledge of the facade and keep no reference to it.


// Selin Doğa Orhan

// Pluggable Authentication Mechanism

import java.util.Scanner;

class User {
	private static int uniqueId = 0; // bu her oluşturulan User'ın unique bir id ye sahip olması için
	private	String name; // user name yani username
	private	String pwd; // user password
	private	int id; // user id

	public User(String _name, String _pwd) {
		this.name = _name;
		this.pwd = _pwd;
		this.id = uniqueId++;
	}
	public String getName() {return name;}
	public int getId() {return id;}
}

class LDAPAuth extends AuthenticationMechanism{
	private static final LDAPAuth instance = new LDAPAuth();
	private static final int LDAPSessionId = 1000;

	private LDAPAuth() {}
	public static LDAPAuth getInstance() {
		return instance;
	}

	@Override
	protected void prepareAuthenticating() {
		System.out.println("Authentication started for Lightweight Directory Access Protocol (LDAP) ...");
	}

	@Override
	protected int authenticate(String name, String pwd) {
		if(name.equals("ldap selin") && pwd.equals("ldap doga"))
			return 0;
		else return 1;
	}

	@Override
	protected int getuid(String name) {
		return 0;
	}

	@Override
	protected int setuid(int uid) {
		return uid + LDAPSessionId;
	}


}

class KerberosAuth extends AuthenticationMechanism{
	private static final KerberosAuth instance = new KerberosAuth();
	private static final int KerberosSessionId = 2000;

	private KerberosAuth(){}
	public static KerberosAuth getInstance() {
		return instance;
	}

	@Override
	protected void prepareAuthenticating() {
		System.out.println("Authentication started for third party authentication mechanism (Kerberos)");
	}

	@Override
	protected int authenticate(String name, String pwd) {
		if(name.equals("kerberos selin") && pwd.equals("kerberos doga"))
			return 0;
		else return 1;
	}

	@Override
	protected int getuid(String name) {
		return 0;
	}

	@Override
	protected int setuid(int uid) {
		return uid + KerberosSessionId;
	}

}

class LocalAuth extends AuthenticationMechanism{
	private static final LocalAuth instance = new LocalAuth();
	private static final int LocalSessionId = 3000;

	private LocalAuth(){}
	public static LocalAuth getInstance() {
		return instance;
	}

	@Override
	protected void prepareAuthenticating() {
		System.out.println("Authentication started on Local File System");
	}

	@Override
	protected int authenticate(String name, String pwd) {
		if(name.equals("local selin") && pwd.equals("local doga"))
			return 0;
		else return 1;
	}

	@Override
	protected int getuid(String name) {
		return 0;
	}

	@Override
	protected int setuid(int uid) {
		return uid + LocalSessionId;
	}


}

// Facade. "Authentication"
// Facade ama yeni özellik ekliyor
// Tüm authenticate yöntemlerine burada bakıyor
class Authentication {
	private static final Authentication instance = new Authentication();
	private static int sessionId; // bu kullanıcı ve authentication özelinde unique oluyor
	private LDAPAuth ldapAuth;
	private KerberosAuth kerberosAuth;
	private LocalAuth localAuth;

	private Authentication() {
		ldapAuth = LDAPAuth.getInstance();
		kerberosAuth = KerberosAuth.getInstance();
		localAuth = LocalAuth.getInstance();
	}

	public static Authentication getInstance(){
		return instance;
	}

	private String getusername(){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter your name please, then press enter: ");
		String r = input.nextLine();
		return r;
	}

	private String getpassword(){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter your password please, then press enter: ");
		String ar = input.nextLine();
		return ar;
	}

	public void LogIn(){
		System.out.println("Welcome, You will be logging in to your OS. \n\tTo get started you need to enter your login credentials.");
		String name = getusername();
		String pwd = getpassword();

		// alınan isim ve password tüm authentication yöntemlerinde bakılıyor

		int ldap = ldapAuth.startAuthentication(name, pwd);
		if(ldap == 0){
			System.out.println("Authentication successful in LDAP.");
			int uid = ldapAuth.getuid(name);
			sessionId = ldapAuth.setuid(uid);

		} else {  // ...

			int kerberos = kerberosAuth.startAuthentication(name, pwd);
			if(kerberos == 0){
				System.out.println("Authentication successful in Kerberos.");
				int uid = kerberosAuth.getuid(name);
				sessionId = kerberosAuth.setuid(uid);

			} else {

				int local = localAuth.startAuthentication(name, pwd);
				if(local == 0){
					System.out.println("Authentication successful in Local File System.");
					int uid = localAuth.getuid(name);
					sessionId = localAuth.setuid(uid);
				}
				else{
					System.out.println("Your authentication failed for all authentication mechanisms.\nYour login credentials are not found in any authentication mechanism. \n\tTherefore you are not logged in. Sorry...");
				}
			}
		}
	}

}

public class OperatingSystem {
	public static void main(String[] args) {
		//Facade
		Authentication authentication = Authentication.getInstance();
		authentication.LogIn();
	}
}
