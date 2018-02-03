package com.amx.jax.tunnel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TunnelSubscriberImpl extends TunnelSubscriber<String> {

	public static List<String> messageList = new ArrayList<String>();
}