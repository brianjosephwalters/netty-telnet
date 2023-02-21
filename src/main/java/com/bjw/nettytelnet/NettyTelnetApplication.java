package com.bjw.nettytelnet;

import com.bjw.nettytelnet.server.MyTcpServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class NettyTelnetApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyTelnetApplication.class, args);
	}

	private final MyTcpServer tcpServer;

	@Bean
	public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
		return event -> tcpServer.start();
	}

}
