package cuj.ejabberd.config;
/*
*
* project_name: DemoOfSmack
* class_name: 
* class_description: 
* author: cujamin
* create_time: 2016年7月13日
* modifier: 
* modify_time: 
* modify_description: 
* @version
*
 */

public class EjabberdConfig {
	static private EjabberdConfig instance = null;
	private String serviceName ;
	private String host ;
	private int port ;
	private String userName ;
	private String passWord ;
	private String type ;
	private String status ;
	
	private String toUserName ;
	private String toServiceName ;
	
	static public EjabberdConfig getInstance(String serviceName , String host , int port)
	{
		if(instance == null)
		{
			instance = new EjabberdConfig(serviceName,host,port);
		}
		return instance;
	}
	static public EjabberdConfig getInstance()
	{
		return instance;
	}
	
	private EjabberdConfig(String serviceName , String host , int port )
	{
		this.serviceName = serviceName;
		this.host = host;
		this.port = port;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatu() {
		return status;
	}
	public void setStatu(String status) {
		this.status = status;
	}
	
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getToServiceName() {
		return toServiceName;
	}
	public void setToServiceName(String toServiceName) {
		this.toServiceName = toServiceName;
	}
	public void setToUser(String toUserName , String toServiceName)
	{
		this.toUserName = toUserName;
		this.toServiceName = toServiceName;
	}
}
