## [디자인 시스템](https://www.figma.com/design/2HuhQQqKTYg2vWpRnvPKDI/%EB%BD%80%EB%AA%A8%EB%83%A5-%EB%94%94%EC%9E%90%EC%9D%B8%EC%8B%9C%EC%8A%A4%ED%85%9C?node-id=66-2511&t=DsFEKTPD8BlUH67R-4) 분배

> 우선순위로 배치
>
[ ]  타이포 → 효민
B나 SB R 같은 경우는 풀어쓴다? + `_` 도 없앤다
    ex) Body_SB → BodySemiBold
[ ]  컬러 토큰 → 지훈
[ ]  Spacing / Border / Radius → 지훈
    간단하게 object로 구현

      ex) Spacing.xSmall → 4.dp


수요일만나기전까지?

---

<aside>
💡 아래 컴포넌트들은 프리뷰 필수

</aside>

[ ]  Icon → 효민
    Icon의 경우 ImageVector로 통일
    24x24를 기본으로 하고 Large Small 등 디자인에서 맞춘 네이밍으로 기본 IconWrapper를 만들어서 사용
        Icon에서 열려있는 것들은 전부 다 열어주기
        ex) PmBigIcon / PmSmallIcon

  > Size Token 포함
>
[ ]  NavigationBar → 알아서
[ ]  Button → 알아서
    [ ]  Toggle

  > Size Token 포함
>
[ ]  Tooltip → 지훈
    dim 처리 등 좀 빡셈
[ ]  Time → 알아서
[ ]  Dialog → 알아서

---

뷰 분배는 위 작업이 어느정도 끝나고 나서 분배

CD는 살짝 미뤄두기