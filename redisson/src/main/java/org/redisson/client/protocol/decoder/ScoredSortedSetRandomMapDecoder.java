/**
 * Copyright (c) 2013-2024 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.client.protocol.decoder;

import org.redisson.client.codec.Codec;
import org.redisson.client.codec.DoubleCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * @author Nikita Koksharov
 *
 */
public class ScoredSortedSetRandomMapDecoder extends ObjectMapReplayDecoder<Object, Object> {

    @Override
    public Decoder<Object> getDecoder(Codec codec, int paramNum, State state, long size) {
        if (paramNum % 2 == 0) {
            return super.getDecoder(codec, paramNum, state, size);
        }
        return DoubleCodec.INSTANCE.getValueDecoder();
    }

    @Override
    public Map<Object, Object> decode(List<Object> parts, State state) {
        if (!parts.isEmpty() && parts.get(0) instanceof Map) {
            return ((List<Map<Object, Object>>) (Object) parts)
                    .stream()
                    .flatMap(v -> v.entrySet().stream())
                    .collect(Collectors.toMap(v -> v.getKey(), v -> v.getValue()));
        }

        return super.decode(parts, state);
    }
}
