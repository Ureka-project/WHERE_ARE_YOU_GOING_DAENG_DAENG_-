import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    scenarios: {
        constant_request: {
            executor: 'constant-arrival-rate',
            rate: 833, // 1초에 833개의 요청을 보내는 설정
            duration: '1m', // 1분 동안 실행
            preAllocatedVUs: 100, // 가상 사용자는 100개 미리 할당
        },
    }
};

export default function () {
    const url = 'http://localhost:8080/api/v1/places/location'; // 테스트 대상 URL
    const params = {
        latitude: 37.55558921,
        longitude: 126.981204,
    };

    // 요청 전송
    const response = http.get(`${url}?latitude=${params.latitude}&longitude=${params.longitude}`);

    // 결과 확인 (응답 시간이 200ms 이상이면 경고)
    if (response.timings.duration > 200) {
        console.error(`Response time exceeded 200ms: ${response.timings.duration}ms`);
    }

    sleep(0.1); // 가상 유저당 대략적인 지연 (0.1초)
}
