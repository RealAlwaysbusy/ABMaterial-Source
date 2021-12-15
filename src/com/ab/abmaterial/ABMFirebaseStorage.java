package com.ab.abmaterial;

import java.io.IOException;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;

@Author("Alain Bailleul")
public class ABMFirebaseStorage {
	protected ABMPage page=null;
	
	protected void SetPage(ABMPage page) {
		this.page = page;
	}
	
	protected void CleanUp() {
		this.page = null;
	}
	
	/**
	 * storagePath: e.g. images/filename.jpg
	 * input: an ABMInput component that has a selected file in it
	 * metaData: extra meta data e.g. "{contentType: 'image/jpeg'}" 
	 */
	public void Upload(String jobId, String storagePath, ABMFileInput input, String metaData) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		s.append("var storage = firebase.storage();");
		s.append("var storageRef = storage.ref();");
		s.append("var metadata = " + metaData + ";");
		
		s.append("var inp = document.getElementById('" + input.ParentString + input.ArrayName.toLowerCase() + input.ID.toLowerCase() + "buttoninput');");
		s.append("var file = inp.files[0];");
		
		s.append("var uploadTask=storageRef.child('" + storagePath + "').put(file,metadata);");
		s.append("console.log(\"uploading...\");");
		s.append("uploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED, function(snapshot) {");
		
		s.append("var progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;");
		s.append("switch (snapshot.state) {");
		s.append("case firebase.storage.TaskState.PAUSED:");
		s.append("break;");
		s.append("case firebase.storage.TaskState.RUNNING:");
		s.append("break;");
		
		s.append("}");
		s.append("}, function(error) {");
			s.append("var errorCode = error.code;");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageerror','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': errorCode});");
		s.append("}, function() {");
			s.append("var downloadURL = uploadTask.snapshot.downloadURL;");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageresult','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': downloadURL});");
		s.append("});");
		
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("Upload");}
				page.ws.Flush();page.RunFlushed();
				}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}	
	
	/**
	 * storagePath: e.g. images/filename.jpg
	 */
	public void Delete(String jobId, String storagePath) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		s.append("var storage = firebase.storage();");
		s.append("var storageRef = storage.ref();");
		s.append("var desertRef = storageRef.child('" + storagePath + "');");
		s.append("desertRef.delete().then(function() {");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageresult','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': ''});");
		s.append("}).catch(function(error) {");
			s.append("var errorCode = error.code;");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageerror','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': errorCode});");
		s.append("});");
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("Delete");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * storagePath: e.g. images/filename.jpg
	 */
	public void GetDownloadUrl(String jobId, String storagePath) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		s.append("var storage = firebase.storage();");
		s.append("var storageRef = storage.ref();");
		s.append("var starsRef = storageRef.child('" + storagePath + "');");
		s.append("starsRef.getDownloadURL().then(function(url) {");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageresult','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': url});");			
		s.append("}).catch(function(error) {");
			s.append("var errorCode = error.code;");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageerror','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': errorCode});");
		s.append("});");
		
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GetdownloadUrl");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * storagePath: e.g. images/filename.jpg
	 */
	public void GetMetaData(String jobId, String storagePath) {
		if (page==null) return;
		if (page.ws==null) return;
		StringBuilder s = new StringBuilder();
		s.append("var storage = firebase.storage();");
		s.append("var storageRef = storage.ref();");
		s.append("var forestRef = storageRef.child('" + storagePath + "');");
		s.append("forestRef.getMetadata().then(function(metadata) {");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageresult','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': JSON.stringify(metadata)});");
		s.append("}).catch(function(error) {");
			s.append("var errorCode = error.code;");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageerror','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': errorCode});");
			s.append("return '';");
		s.append("});");
		
		page.ws.Eval(s.toString(), null);
		try {
			if (page.ws.getOpen()) {
				if (page.ShowDebugFlush) {BA.Log("GetMetaData");}
				page.ws.Flush();page.RunFlushed();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * storagePath: e.g. images/filename.jpg
	 * metaData: extra meta data e.g. "{contentType: 'image/jpeg',cacheControl: 'public,max-age=300'}" 
	 */
	public void UpdateMetaData(String jobId, String storagePath, String metaData) {
		if (page==null) return ;
		if (page.ws==null) return ;
		StringBuilder s = new StringBuilder();
		s.append("var storage = firebase.storage();");
		s.append("var storageRef = storage.ref();");
		s.append("var forestRef = storageRef.child('" + storagePath + "');");
		s.append("var newMetadata = " + metaData + ";");
		s.append("forestRef.updateMetadata(newMetadata).then(function(metadata) {");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageresult','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': ''});");
		s.append("}).catch(function(error) {");
			s.append("var errorCode = error.code;");
			s.append("b4j_raiseEvent('page_parseevent', {'eventname': 'page_firebasestorageerror','eventparams': 'jobid,extra', 'jobid': '" + jobId + "', 'extra': errorCode});");
		s.append("});");
	}
}
