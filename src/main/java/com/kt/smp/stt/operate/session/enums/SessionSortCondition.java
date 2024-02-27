/**
 * 
 */
package com.kt.smp.stt.operate.session.enums;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kt.smp.stt.operate.session.dto.SessionInfoDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SessionSortCondition {

	NONE("없음", doNothingComparator()),
	SESSION_HOLDING_TIME_DESC("통화시간 내림", sessionHoldingTimeDescComparator()),
	CALL_KEY_ASC("내선번호 오름", callKeyAscComparator()),
	CALL_KEY_DESC("내선번호 내림", callKeyDescComparator())
	;
	
	private final String description;
	private final Comparator<SessionInfoDto> comparator;
	
	private static final Map<String, SessionSortCondition> descriptions = Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(SessionSortCondition::getDescription, Function.identity())));	
	
	public static List<Map<String, String>> getMapList() {
		return Stream.of(values()).map(cond -> {
			return Map.of("key", cond.name(), "value", cond.getDescription());
			}).collect(Collectors.toList());
	}
	
	public static SessionSortCondition find(String description) {
		return Optional.ofNullable(descriptions.get(description)).orElse(NONE);
	}
	
	private static final Comparator<SessionInfoDto> doNothingComparator() {
		return (o1, o2) -> 0;
	}
	
	private static final Comparator<SessionInfoDto> sessionHoldingTimeDescComparator() {
		return new Comparator<SessionInfoDto>() {
			@Override
			public int compare(SessionInfoDto o1, SessionInfoDto o2) {
				long o1SessionHoldingTime = o1.getSessionHoldingTime() / 1000;
				long o2SessionHoldingTime = o2.getSessionHoldingTime() / 1000;
				
				if (o1SessionHoldingTime < o2SessionHoldingTime) {
					return 1;
				} else if (o1SessionHoldingTime > o2SessionHoldingTime) {
					return -1;
				}
				return 0;
			}
		};
	}
	
	private static final Comparator<SessionInfoDto> callKeyAscComparator() {
		return new Comparator<SessionInfoDto>() {
			@Override
			public int compare(SessionInfoDto o1, SessionInfoDto o2) {
				
				String o1CallKey = o1.getCallKey();
				String o2CallKey = o2.getCallKey();
				
				String prefix1 = o1CallKey.replaceAll("\\d+", "");
				String prefix2 = o2CallKey.replaceAll("\\d+", "");
				
	            String numberPart1 = o1CallKey.substring(prefix1.length());
	            String numberPart2 = o2CallKey.substring(prefix2.length());
	            
	            int stringCompare = prefix1.compareTo(prefix2);
	            if (stringCompare != 0) {
	                return stringCompare;
	            }
	
	            // 문자열 부분이 같다면 숫자 부분을 비교
	            // 숫자 부분을 정수로 변환하여 비교
	            return Integer.compare(Integer.valueOf(numberPart1), Integer.valueOf(numberPart2));
			}
		};
	}
	
	private static final Comparator<SessionInfoDto> callKeyDescComparator() {
		return callKeyAscComparator().reversed();
	}
}
