package org.tongji.programming.DTO.cqhttp.cqhttp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    @JsonProperty("packet_received")
    private long packetReceived;

    @JsonProperty("packet_sent")
    private long packetSent;

    @JsonProperty("packet_lost")
    private int packetLost;

    @JsonProperty("message_received")
    private long messageReceived;

    @JsonProperty("message_sent")
    private long messageSent;

    @JsonProperty("disconnect_times")
    private int disconnectTimes;

    @JsonProperty("lost_times")
    private int lostTimes;

    @JsonProperty("last_message_time")
    private long lastMessageTime;
}
