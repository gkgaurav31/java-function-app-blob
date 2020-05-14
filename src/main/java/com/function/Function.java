package com.function;

import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import java.io.*;
/**
 * Azure Functions with HTTP Trigger.
 */

//DefaultEndpointsProtocol=https;AccountName=gaukstorage123;AccountKey=/aXJPKBtdJMRFKCGrU46tNlk8veX5TFbX45dMExTI2lG5dstkfIrBLbEaLdXLYPeiV4OJluQ39WQAj2dnSE2WQ==;EndpointSuffix=core.windows.net


public class Function {
	/**
	 * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
	 * 1. curl -d "HTTP Body" {your host}/api/HttpExample
	 * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
	 */
	@FunctionName("HttpExample")
	public HttpResponseMessage run(
			@HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
			final ExecutionContext context) {
		context.getLogger().info("Java HTTP trigger processed a request.");


		//start

		System.out.println("Azure Blob storage v12 - Java quickstart sample\n");
		//String connectStr = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
		String connectStr = "<Connection String>";
		
		// Create a BlobServiceClient object which will be used to create a container client
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();

		//Create a unique name for the container
		String containerName = "testblob";

		// Create the container and return a container client object
		//BlobContainerClient containerClient = blobServiceClient.createBlobContainer(containerName);
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

		System.out.println("\nListing blobs...");
		
		System.out.println(containerClient.getBlobClient("hello.txt").getBlobUrl());
		
		
		// List the blob(s) in the container.
		for (BlobItem blobItem : containerClient.listBlobs()) {
		    System.out.println("\t" + blobItem.getName());
		    context.getLogger().info("\t" + blobItem.getName());
		}
		
		//end


		// Parse query parameter
		String query = request.getQueryParameters().get("name");
		String name = request.getBody().orElse(query);

		if (name == null) {
			return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
		} else {
			return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
		}
	}
}
