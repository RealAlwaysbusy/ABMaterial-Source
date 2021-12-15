package com.ab.abmaterial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;

/**
 * Can not be created within B4J, But its properties and methods are available through another ABM class.
 */
@Author("Alain Bailleul")
public class NativeApp {
	protected String PackageName="";
	protected String ApplicationName="";
	protected String ApplicationTitle="";
	protected String ABMaterialURL="";
	
	public String DesktopIcon256x256PngPath="";	
	public String AndroidIcon72x72PngPath="";
			
	public String VersionCode="1";
	public String VersionName="1.0.0";
	
	public String NoConnectionMessage="You need a connection to the internet to run this app!";
	public String NoConnectionRetry = "Retry";
	public String NoConnectionCancel = "Cancel";
			
	public String QuitMessage = "Do you want to exit the app?";
	public String QuitYes = "Yes";
	public String QuitNo = "No";
	
	protected boolean IsInitialized=false;
	
	public void Initialize(String packageName, String applicationName, String applicationTitle, String ABMaterialWebAppUrl) {
		if (packageName.equals("")) {
			BA.LogError("No packageName provided!");
			return;
		}
		if (packageName.indexOf(".") == -1) {
			BA.LogError("Warning: packageName should be in format: com.mysite.myapp");			
		}
		if (applicationName.equals("")) {
			BA.LogError("No applicationName provided!");
			return;
		}
		if (applicationName.indexOf(" ")>-1) {
			BA.LogError("Warning: best not to use a space in the name.");			
		}
		if (!ABMaterialWebAppUrl.toLowerCase().startsWith("http")) {
			BA.LogError("ABMaterialWebAppUrl should be the http address to your WebApp!");
			return;
		}		
		this.PackageName = packageName;
		this.ApplicationName = applicationName;
		this.ApplicationTitle = applicationTitle;
		this.ABMaterialURL = ABMaterialWebAppUrl;
		IsInitialized=true;
	}
	
	protected void Generate(ABMPage page, boolean DoMobileAppGenerationOverwrite) {		
		if (!IsInitialized) {
			BA.LogError("Generator is not initialized!");
			return;
		}
		
		AndroidApp(DoMobileAppGenerationOverwrite);
		DesktopApp(DoMobileAppGenerationOverwrite);
		
		if (DoMobileAppGenerationOverwrite) {
			BA.LogError("--------------------------------------------------------------------------------------------------------");
			BA.LogError("DO NOT FORGET TO DISABLE OVERWRITE in ABM.NativeGenerateApps if you wll make changes to the Native Apps!");
			BA.LogError("--------------------------------------------------------------------------------------------------------");
		}		
	}
	
	protected void AndroidApp(boolean DoMobileAppGenerationOverwrite) {
		BA.Log("Generating B4A android app...");
		String dirApp = anywheresoftware.b4a.objects.streams.File.getDirApp();
		File SourceFolder=null;
		SourceFolder = new File(dirApp);
		SourceFolder = new File(SourceFolder.getParent()); // .bas files
    	
		String pageDir = SourceFolder.getAbsolutePath() + File.separator + "NativeApps";
		
		try {
			Files.createDirectories(Paths.get(pageDir));
		} catch (IOException e) {
			
		}
		pageDir = pageDir + File.separator + "B4A";
		if (Files.exists(Paths.get(pageDir))) {
			if (!DoMobileAppGenerationOverwrite) {
				BA.Log("B4A android app already exists in " + pageDir + ". No overwriting.");
				return;
			}
		}
		rmdir(new File(pageDir));
		try {
			Files.createDirectories(Paths.get(pageDir));
			Files.createDirectories(Paths.get(pageDir + File.separator + "Files"));
			Files.createDirectories(Paths.get(pageDir + File.separator + "Objects"));
			Files.createDirectories(Paths.get(pageDir + File.separator + "Objects" + File.separator + "res"));
			Files.createDirectories(Paths.get(pageDir + File.separator + "Objects" + File.separator + "res" + File.separator + "drawable"));
		} catch (IOException e) {
		}   
		
		WriteB4A(pageDir);
		WriteB4AMeta(pageDir);
		WriteB4AStarterBas(pageDir);
		WriteB4AXml(pageDir + File.separator + "Objects");
		if (!AndroidIcon72x72PngPath.equals("")) {
			try {
				Files.copy(new File(AndroidIcon72x72PngPath).toPath(), new File(pageDir + File.separator + "Objects" + File.separator + "res" + File.separator + "drawable" + File.separator + "icon.png").toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ExportResource("resources/android/", "icon.png", pageDir + File.separator + "Objects" + File.separator + "res" + File.separator + "drawable" ); //+ File.separator + "icon.png");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// write icon'
		try {
			ExportResource("resources/android/", "viewerlayout.bal", pageDir + File.separator + "Files");
		} catch (Exception e) {
			e.printStackTrace();
		}
		BA.Log("B4A android app generated in " + pageDir);
		// end android
	}
	
	protected void DesktopApp(boolean DoMobileAppGenerationOverwrite) {
		BA.Log("Generating B4J desktop app...");
		String dirApp = anywheresoftware.b4a.objects.streams.File.getDirApp();
		File SourceFolder=null;
		SourceFolder = new File(dirApp);
		SourceFolder = new File(SourceFolder.getParent()); // .bas files
    	
		String pageDir = SourceFolder.getAbsolutePath() + File.separator + "NativeApps";
		
		try {
			Files.createDirectories(Paths.get(pageDir));
		} catch (IOException e) {
			
		}
		pageDir = pageDir + File.separator + "B4J";
		if (Files.exists(Paths.get(pageDir))) {
			if (!DoMobileAppGenerationOverwrite) {
				BA.Log("B4J desktop app already exists in " + pageDir + ". No overwriting.");
				return;
			}
		}
		rmdir(new File(pageDir));
		try {
			Files.createDirectories(Paths.get(pageDir));
			Files.createDirectories(Paths.get(pageDir + File.separator + "Files"));
			Files.createDirectories(Paths.get(pageDir + File.separator + "Objects"));			
		} catch (IOException e) {
		}   
		
		WriteB4J(pageDir);
		WriteB4JMeta(pageDir);
		if (!DesktopIcon256x256PngPath.equals("")) {
			try {
				Files.copy(new File(DesktopIcon256x256PngPath).toPath(), new File(pageDir + File.separator + "Files" + File.separator + "icon.png").toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ExportResource("resources/desktop/", "icon.png", pageDir + File.separator + "Files");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// write icon'
		try {
			ExportResource("resources/desktop/", "ViewerLayout.bjl", pageDir + File.separator + "Files");
		} catch (Exception e) {
			e.printStackTrace();
		}
		BA.Log("B4J desktop app generated in " + pageDir);
		// end android
	}
	
	protected void ExportResource(String path, String resourceName, String pageDir) throws Exception {
	        InputStream stream = null;
	        OutputStream resStreamOut = null;
	        try {
	            stream = this.getClass().getClassLoader().getResourceAsStream(path + resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
	            if(stream == null) {
	            	BA.LogError("Cannot get resource \"" + path + resourceName + "\" from ABMaterial library file.");
	            	return;	                
	            }

	            int readBytes;
	            byte[] buffer = new byte[4096];
	            resStreamOut = new FileOutputStream(pageDir + File.separator + resourceName);
	            while ((readBytes = stream.read(buffer)) > 0) {
	                resStreamOut.write(buffer, 0, readBytes);
	            }
	        } catch (Exception ex) {
	        	ex.printStackTrace();
            	return;
	        } finally {
	            stream.close();
	            resStreamOut.close();
	        }	}
	
	protected void WriteB4A(String pageDir) {
		BufferedWriter writer = null;
		try {
        	File buildFile = new File(pageDir, ApplicationName + ".b4a");
        	writer = new BufferedWriter(new FileWriter(buildFile));
        	
        	StringBuilder s =new StringBuilder();
        	
        	s.append("Version=6.5\r\n");
        	s.append("NumberOfModules=1\r\n");
        	s.append("Module1=Starter\r\n");
        	s.append("Build1=Default," + PackageName + "\r\n");
        	s.append("ManifestCode='This code will be applied to the manifest file during compilation.~\\n~'You do not need to modify it in most cases.~\\n~'See this link for for more information: http://www.b4x.com/forum/showthread.php?p=78136~\\n~AddManifestText(~\\n~<uses-sdk android:minSdkVersion=\"5\" android:targetSdkVersion=\"19\"/>~\\n~<supports-screens android:largeScreens=\"true\" ~\\n~    android:normalScreens=\"true\" ~\\n~    android:smallScreens=\"true\" ~\\n~    android:anyDensity=\"true\"/>)~\\n~SetApplicationAttribute(android:icon, \"@drawable/icon\")~\\n~SetApplicationAttribute(android:label, \"$LABEL$\")~\\n~'End of default text.~\\n~\r\n");
        	s.append("IconFile=\r\n");
        	s.append("NumberOfFiles=1\r\n");
        	s.append("File1=ViewerLayout.bal\r\n");
        	s.append("NumberOfLibraries=2\r\n");
        	s.append("Library1=core\r\n");
        	s.append("Library2=abmcontrollerb4a\r\n");
        	s.append("@EndOfDesignText@\r\n");
        	s.append("#Region Project Attributes\r\n"); 
        	s.append("\t#ApplicationLabel: " + ApplicationTitle + "\r\n");
        	s.append("\t#VersionCode: 1\r\n");
        	s.append("\t#VersionName: 1.0\r\n");
        	s.append("\t'SupportedOrientations possible values: unspecified, landscape or portrait.\r\n");
        	s.append("\t#SupportedOrientations: unspecified\r\n");
        	s.append("\t#CanInstallToExternalStorage: False\r\n");
        	s.append("#End Region\r\n");
        	s.append("\r\n");
        	s.append("#Region Activity Attributes\r\n"); 
        	s.append("\t#FullScreen: true\r\n");
        	s.append("\t#IncludeTitle: false\r\n");
        	s.append("#End Region\r\n");
        	s.append("\r\n");
        	s.append("Sub Process_Globals\r\n");
        	s.append("\t'These global variables will be declared once when the application starts.\r\n");
        	s.append("\t'These variables can be accessed from all modules.\r\n");
        	s.append("\tDim ABMaterialURL As String = \"" + ABMaterialURL + "\" ' your app url\t\r\n");
        	s.append("\t\r\n");
        	s.append("\tDim NoConnectionTitle As String = \"" + ApplicationTitle + "\"\r\n");
        	s.append("\tDim NoConnectionMessage As String = \"" + NoConnectionMessage + "\"\r\n");
        	s.append("\tDim NoConnectionRetry As String = \"" + NoConnectionRetry + "\"\r\n");
        	s.append("\tDim NoConnectionCancel As String = \"" + NoConnectionCancel + "\"\r\n");
        	s.append("\t\r\n");
        	s.append("\tDim QuitTitle As String = \"" + ApplicationTitle + "\"\r\n");
        	s.append("\tDim QuitMessage As String = \"" + QuitMessage + "\"\r\n");
        	s.append("\tDim QuitYes As String = \"" + QuitYes + "\"\r\n");
        	s.append("\tDim QuitNo As String = \"" + QuitNo + "\"\r\n");
        	s.append("\r\n");
        	s.append("\tDim currentMessage As ABMControllerMessage\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Globals\r\n");
        	s.append("\t'These global variables will be redeclared each time the activity is created.\r\n");
        	s.append("\t'These variables can only be accessed from this module.\r\n");
        	s.append("\tDim Viewer As WebView\r\n");
        	s.append("\tDim currentMessage As ABMControllerMessage\r\n");
        	s.append("\tDim controller As ABMController\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	
        	s.append("Sub Activity_Create(FirstTime As Boolean)\r\n");
        	s.append("\t'Do not forget to load the layout file created with the visual designer. For example:\r\n");
        	s.append("\tActivity.LoadLayout(\"ViewerLayout\")\r\n");
        	s.append("\r\n");
        	s.append("\tcontroller.Initialize(\"controller\", ABMaterialURL)\r\n");	
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Controller_OnlineResult(IsOnline As Boolean)\r\n");
        	s.append("\tIf IsOnline = False Then\r\n");
        	s.append("\t\tDim ret As Int = Msgbox2(NoConnectionMessage, NoConnectionTitle,NoConnectionRetry, \"\", NoConnectionCancel, Null)\r\n");
        	s.append("\t\tIf ret = DialogResponse.NEGATIVE Then\r\n");
        	s.append("\t\t\tExitApplication\r\n");
        	s.append("\t\tElse\r\n");
        	s.append("\t\t\tcontroller.IsOnline(ABMaterialURL)\r\n");
        	s.append("\t\tEnd If\r\n");
        	s.append("\tElse\r\n");
        	s.append("\t\tViewer.LoadUrl(ABMaterialURL)\r\n");		
        	s.append("\tEnd If\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub InitializeViewer\r\n");
        	s.append("\tViewer.Left = 0\r\n");
        	s.append("\tViewer.Top = 0\r\n");
        	s.append("\tViewer.Height = Activity.Height\r\n");
        	s.append("\tViewer.Width = Activity.Width\r\n");
        	s.append("\tViewer.ZoomEnabled = False\r\n");
        	s.append("\tViewer.Visible = True\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Activity_Resume\r\n");
        	s.append("\tInitializeViewer\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Activity_Pause (UserClosed As Boolean)\r\n");
        	s.append("\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event\r\n");
        	s.append("\tIf KeyCode = KeyCodes.KEYCODE_BACK Then\r\n");
        	s.append("\t\tDim ret As Int = Msgbox2(QuitMessage, QuitTitle,QuitYes, \"\", QuitNo, Null)\r\n");
        	s.append("\t\tIf ret = DialogResponse.POSITIVE Then\r\n");
        	s.append("\t\t\tExitApplication\r\n");
        	s.append("\t\tEnd If\r\n");
        	s.append("\t\tReturn True\r\n");
        	s.append("\tEnd If\r\n");
        	s.append("\tReturn False\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Viewer_PageFinished (Url As String)\r\n");
        	s.append("\tcontroller.StartController(Viewer, Me)\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub NativeRequest(jsonMessage As String) 'ignore\r\n");
        	s.append("\tcurrentMessage.Initialize\r\n");
        	s.append("\tcurrentMessage.FromJSON(jsonMessage)\r\n");
        	s.append("\tSelect Case currentMessage.Action\r\n");
        	s.append("\t\t'Case \"Test\"\r\n");
        	s.append("\t\t\t'currentMessage.Parameters.Clear\r\n");
        	s.append("\t\t\t'currentMessage.Parameters.Add(\"Hello from the native App!\")\r\n");
        	s.append("\t\t\t'currentMessage.Status=controller.STATUS_WITHRESPONSE\r\n");
        	s.append("\t\tCase Else\r\n");
        	s.append("\t\t\tcurrentMessage.Status=controller.STATUS_NORESPONSE\r\n");
        	s.append("\tEnd Select\r\n");
        	s.append("\tcontroller.NativeResponse(currentMessage.ToJSON)\r\n");
        	s.append("End Sub\r\n");
        	
        	writer.write(s.toString());
        	
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	protected void WriteB4AMeta(String pageDir) {
		BufferedWriter writer = null;
		try {
        	File buildFile = new File(pageDir, ApplicationName + ".b4a.meta");
        	writer = new BufferedWriter(new FileWriter(buildFile));
        	
        	StringBuilder s =new StringBuilder();
        	
        	s.append("VisibleModules=1\r\n");
        	s.append("ModuleBreakpoints0=\r\n");
        	s.append("ModuleBookmarks0=\r\n");
        	s.append("ModuleClosedNodes0=\r\n");
        	s.append("ModuleBreakpoints1=\r\n");
        	s.append("ModuleBookmarks1=\r\n");
        	s.append("ModuleClosedNodes1=1\r\n");
        	s.append("SelectedBuild=0\r\n");
        		
        	writer.write(s.toString());
        	
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
		
	protected void WriteB4AStarterBas(String pageDir) {
		BufferedWriter writer = null;
		try {
        	File buildFile = new File(pageDir, "Starter.bas");
        	writer = new BufferedWriter(new FileWriter(buildFile));
        	
        	StringBuilder s =new StringBuilder();
        	
        	s.append("Type=Service\r\n");
        	s.append("Version=6.5\r\n");
        	s.append("ModulesStructureVersion=1\r\n");
        	s.append("B4A=true\r\n");
        	s.append("@EndOfDesignText@\r\n");
        	s.append("#Region Service Attributes\r\n"); 
        	s.append("\t#StartAtBoot: False\r\n");
        	s.append("\t#ExcludeFromLibrary: True\r\n");
        	s.append("#End Region\r\n");
        	s.append("\r\n");
        	s.append("Sub Process_Globals\r\n");
        	s.append("\t'These global variables will be declared once when the application starts.\r\n");
        	s.append("\t'These variables can be accessed from all modules.\r\n");
        	s.append("\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Service_Create\r\n");
        	s.append("\t'This is the program entry point.\r\n");
        	s.append("\t'This is a good place to load resources that are not specific to a single activity.\r\n");
        	s.append("\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Service_Start (StartingIntent As Intent)\r\n");
        	s.append("\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Service_TaskRemoved\r\n");
        	s.append("\t'This event will be raised when the user removes the app from the recent apps list.\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("'Return true to allow the OS default exceptions handler to handle the uncaught exception.\r\n");
        	s.append("Sub Application_Error (Error As Exception, StackTrace As String) As Boolean\r\n");
        	s.append("\tReturn True\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Service_Destroy\r\n");
        	s.append("\r\n");
        	s.append("End Sub\r\n");
                		
        	writer.write(s.toString());
        	
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	protected void WriteB4AXml(String pageDir) {
		BufferedWriter writer = null;
		try {
        	File buildFile = new File(pageDir, "AndroidManifest.xml");
        	writer = new BufferedWriter(new FileWriter(buildFile));
        	
        	StringBuilder s =new StringBuilder();
        	
        	s.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        	s.append("<manifest\r\n");
        	s.append("\txmlns:android=\"http://schemas.android.com/apk/res/android\"\r\n");
        	s.append("\tpackage=\"com.abmaterial.myapp\"\r\n");
        	s.append("\tandroid:versionCode=\"1\"\r\n");
        	s.append("\tandroid:versionName=\"1.0\"\r\n");
        	s.append("\tandroid:installLocation=\"internalOnly\">\r\n");
        	s.append("\t\r\n");
        	s.append("\t<uses-sdk android:minSdkVersion=\"5\" android:targetSdkVersion=\"19\"/>\r\n");
        	s.append("\t<supports-screens android:largeScreens=\"true\" \r\n");
        	s.append("\t    android:normalScreens=\"true\" \r\n");
        	s.append("\t    android:smallScreens=\"true\" \r\n");
        	s.append("\t    android:anyDensity=\"true\"/>\r\n");
        	s.append("\t<uses-permission android:name=\"android.permission.INTERNET\"/>\r\n");
        	s.append("\t<uses-permission android:name=\"android.permission.ACCESS_WIFI_STATE\"/>\r\n");
        	s.append("\t<uses-permission android:name=\"android.permission.ACCESS_NETWORK_STATE\"/>\r\n");        	
        	s.append("\t<application\r\n");
        	s.append("\t\tandroid:icon=\"@drawable/icon\"\r\n");
        	s.append("\t\tandroid:label=\"" + ApplicationTitle + "\">\r\n");
        	s.append("\t\t<activity\r\n");
        	s.append("\t\t\tandroid:windowSoftInputMode=\"stateHidden\"\r\n");
        	s.append("\t\t\tandroid:launchMode=\"singleTop\"\r\n");
        	s.append("\t\t\tandroid:name=\".main\"\r\n");
        	s.append("\t\t\tandroid:label=\"" + ApplicationTitle + "\"\r\n");
        	s.append("\t\t\tandroid:screenOrientation=\"unspecified\">\r\n");
        	s.append("\t\t\t<intent-filter>\r\n");
        	s.append("\t\t\t   <action android:name=\"android.intent.action.MAIN\" />\r\n");
        	s.append("\t\t\t   <category android:name=\"android.intent.category.LAUNCHER\" />\r\n");
        	s.append("\t\t\t</intent-filter>\r\n");
        	s.append("\t\t\t\r\n");
        	s.append("\t\t</activity>\r\n");
        	s.append("\t\t<service android:name=\".starter\">\r\n");
        	s.append("\t\t</service>\r\n");
        	s.append("\t\t<receiver android:name=\".starter$starter_BR\">\r\n");
        	s.append("\t\t</receiver>\r\n");
        	s.append("\t</application>\r\n");
        	s.append("</manifest>\r\n");
                        		
        	writer.write(s.toString());
        	
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	protected void WriteB4J(String pageDir) {
		BufferedWriter writer = null;
		try {
        	File buildFile = new File(pageDir, ApplicationName + ".b4j");
        	writer = new BufferedWriter(new FileWriter(buildFile));
        	
        	StringBuilder s =new StringBuilder();
        	
        	s.append("Version=4.7\r\n");
        	s.append("AppType=JavaFX\r\n");
        	s.append("NumberOfModules=0\r\n");
        	s.append("Build1=Default," + PackageName + "\r\n");
        	s.append("NumberOfFiles=2\r\n");
        	s.append("File1=icon.png\r\n");
        	s.append("File2=ViewerLayout.bjl\r\n");
        	s.append("NumberOfLibraries=3\r\n");
        	s.append("Library1=jcore\r\n");
        	s.append("Library2=jfx\r\n");
        	s.append("Library3=abmcontrollerb4j\r\n");
        	s.append("@EndOfDesignText@\r\n");
        	s.append("#Region Project Attributes \r\n");
        	s.append("\t#MainFormWidth: 1280\r\n");
        	s.append("\t#MainFormHeight: 800\r\n");
        	s.append("#End Region\r\n");
        	s.append("\r\n");
        	s.append("Sub Process_Globals\r\n");
        	s.append("\tPrivate fx As JFX\r\n");
        	s.append("\tPrivate MainForm As Form\r\n");
        	s.append("\tPrivate Viewer As WebView\r\n");
        	s.append("\t\r\n");
        	s.append("\tDim ABMaterialURL As String = \"" + ABMaterialURL + "\" ' your app url\t\r\n");
        	s.append("\t\r\n");
        	s.append("\tDim NoConnectionTitle As String = \"" + ApplicationTitle + "\"\r\n");
        	s.append("\tDim NoConnectionMessage As String = \"" + NoConnectionMessage + "\"\r\n");
        	s.append("\tDim NoConnectionRetry As String = \"" + NoConnectionRetry + "\"\r\n");
        	s.append("\tDim NoConnectionCancel As String = \"" + NoConnectionCancel + "\"\r\n");
        	s.append("\t\r\n");
        	s.append("\tDim QuitTitle As String = \"" + ApplicationTitle + "\"\r\n");
        	s.append("\tDim QuitMessage As String = \"" + QuitMessage + "\"\r\n");
        	s.append("\tDim QuitYes As String = \"" + QuitYes + "\"\r\n");
        	s.append("\tDim QuitNo As String = \"" + QuitNo + "\"\r\n");
        	s.append("\r\n");
        	s.append("\tDim currentMessage As ABMControllerMessage\r\n");
        	s.append("\tDim controller As ABMController\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	
        	s.append("Sub AppStart (Form1 As Form, Args() As String)\r\n");	
        	s.append("\tMainForm = Form1\r\n");
        	s.append("\tMainForm.Icon = fx.LoadImage(File.DirAssets, \"icon.png\")\r\n");
        	s.append("\tMainForm.RootPane.LoadLayout(\"ViewerLayout\") 'Load the layout file.\r\n");
        	s.append("\tViewer.Visible = False\r\n");
        	s.append("\tMainForm.Show\r\n");	
        	s.append("\r\n");
        	s.append("\tcontroller.Initialize(\"controller\", ABMaterialURL)\r\n");	
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Controller_OnlineResult(IsOnline As Boolean)\r\n");
        	s.append("\tIf IsOnline = False Then\r\n");
        	s.append("\t\tDim ret As Int = fx.Msgbox2(MainForm,NoConnectionMessage, NoConnectionTitle,NoConnectionRetry, \"\", NoConnectionCancel, fx.MSGBOX_WARNING)\r\n");
        	s.append("\t\tIf ret = fx.DialogResponse.NEGATIVE Then\r\n");
        	s.append("\t\t\tExitApplication\r\n");			
        	s.append("\t\tElse\r\n");
        	s.append("\t\t\tcontroller.IsOnline(ABMaterialURL)\r\n");
        	s.append("\t\tEnd If\r\n");
        	s.append("\tElse\r\n");
        	s.append("\t\tViewer.LoadUrl(ABMaterialURL)\r\n");
        	s.append("\tEnd If\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Mainform_CloseRequest (EventData As Event)\r\n");
        	s.append("\tDim ret As Int = fx.Msgbox2(MainForm, QuitMessage, QuitTitle,QuitYes, \"\", QuitNo, fx.MSGBOX_WARNING)\r\n");
        	s.append("\tIf ret = fx.DialogResponse.POSITIVE Then\r\n");
        	s.append("\t\tExitApplication\r\n");
        	s.append("\tEnd If\r\n");		
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub Viewer_PageFinished (Url As String)\r\n");
        	s.append("\tViewer.Visible = True\r\n");
        	s.append("\tcontroller.StartController(Viewer, Me)\r\n");
        	s.append("End Sub\r\n");
        	s.append("\r\n");
        	s.append("Sub NativeRequest(jsonMessage As String) 'ignore\r\n");
        	s.append("\tLog(\"got a question!\")\r\n");
        	s.append("\tcurrentMessage.Initialize\r\n");
        	s.append("\tcurrentMessage.FromJSON(jsonMessage)\r\n");
        	s.append("\tSelect Case currentMessage.Action\r\n");
        	s.append("\t\t'Case \"Test\"\r\n");
        	s.append("\t\t\t'currentMessage.Parameters.Clear\r\n");
        	s.append("\t\t\t'currentMessage.Parameters.Add(\"Hello from the native App!\")\r\n");
        	s.append("\t\t\t'currentMessage.Status=controller.STATUS_WITHRESPONSE\r\n");
        	s.append("\t\tCase Else\r\n");	
        	s.append("\t\t\tcurrentMessage.Status=controller.STATUS_NORESPONSE\r\n");		
        	s.append("\tEnd Select\r\n");
        	s.append("\tcontroller.NativeResponse(currentMessage.ToJSON)\r\n");
        	s.append("End Sub\r\n");
        	
        	writer.write(s.toString());
        	
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	protected void WriteB4JMeta(String pageDir) {
		BufferedWriter writer = null;
		try {
        	File buildFile = new File(pageDir, ApplicationName + ".b4j.meta");
        	writer = new BufferedWriter(new FileWriter(buildFile));
        	
        	StringBuilder s =new StringBuilder();
        	
        	s.append("VisibleModules=\r\n");
        	s.append("ModuleBreakpoints0=\r\n");
        	s.append("ModuleBookmarks0=\r\n");
        	s.append("ModuleClosedNodes0=\r\n");
        	s.append("SelectedBuild=0\r\n");
        		
        	writer.write(s.toString());
        	
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
	}
	
	protected static void rmdir(final File folder) {
	      // check if folder file is a real folder
	      if (folder.isDirectory()) {
	          File[] list = folder.listFiles();
	          if (list != null) {
	              for (int i = 0; i < list.length; i++) {
	                  File tmpF = list[i];
	                  if (tmpF.isDirectory()) {
	                      rmdir(tmpF);
	                  }
	                  tmpF.delete();
	              }
	          }
	          if (!folder.delete()) {
	            BA.Log("Can't delete folder : " + folder);
	          }
	      }
	}
}
