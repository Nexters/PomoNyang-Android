package com.pomonyang.mohanyang.presentation.screen.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohanyang.presentation.R
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButton
import com.pomonyang.mohanyang.presentation.designsystem.button.text.MnTextButtonStyles
import com.pomonyang.mohanyang.presentation.designsystem.spinner.MnSpinner
import com.pomonyang.mohanyang.presentation.designsystem.token.MnSpacing
import com.pomonyang.mohanyang.presentation.model.category.PomodoroCategoryModel
import com.pomonyang.mohanyang.presentation.screen.home.category.model.CategoryIcon
import com.pomonyang.mohanyang.presentation.screen.statistics.component.CategoryRankingContent
import com.pomonyang.mohanyang.presentation.screen.statistics.component.StatisticsContentHeader
import com.pomonyang.mohanyang.presentation.screen.statistics.component.StatisticsTopBar
import com.pomonyang.mohanyang.presentation.screen.statistics.component.TotalFocusTimeContent
import com.pomonyang.mohanyang.presentation.screen.statistics.component.datepicker.StatisticDatePickerModal
import com.pomonyang.mohanyang.presentation.screen.statistics.component.datepicker.rememberStatisticDateState
import com.pomonyang.mohanyang.presentation.screen.statistics.component.graph.FocusGraphConfigure
import com.pomonyang.mohanyang.presentation.screen.statistics.component.graph.GraphContainer
import com.pomonyang.mohanyang.presentation.screen.statistics.model.CategoryRankingModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.DailyFocusTimeModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.FocusTimeModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.RankingItemModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.StatisticsModel
import com.pomonyang.mohanyang.presentation.screen.statistics.model.WeeklyFocusTimeTrendModel
import com.pomonyang.mohanyang.presentation.screen.statistics.widget.StatisticsContent
import com.pomonyang.mohanyang.presentation.theme.MnTheme
import com.pomonyang.mohanyang.presentation.util.ThemePreviews
import com.pomonyang.mohanyang.presentation.util.collectWithLifecycle
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun StatisticsRoute(
    onShowSnackbar: (String, Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDatePickerDialog by remember { mutableStateOf(false) }

    viewModel.effects.collectWithLifecycle { effect ->
        when (effect) {
            StatisticsSideEffect.ShowDatePickerDialog -> {
                showDatePickerDialog = true
            }

            StatisticsSideEffect.SideDatePickerDialog -> {
                showDatePickerDialog = false
            }
        }
    }

    StatisticsScreen(
        isLoading = state.isLoading,
        showDatePickerDialog = showDatePickerDialog,
        targetDate = state.targetDate,
        focusTime = state.totalFocusTime,
        focusHistory = state.focusTimes,
        weeklyFocusTime = state.weeklyFocusTimeList.toImmutableList(),
        weeklyMaxFocusTime = state.weeklyMaxFocusTime,
        categoryRankingList = state.categoryRankingList,
        categoryRankingDate = state.categoryRankingDate,
        onAction = viewModel::handleEvent,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsScreen(
    isLoading: Boolean,
    showDatePickerDialog: Boolean,
    targetDate: LocalDate,
    focusTime: Duration,
    focusHistory: ImmutableList<FocusTimeModel>,
    weeklyMaxFocusTime: Float,
    weeklyFocusTime: ImmutableList<Float>,
    categoryRankingList: ImmutableList<RankingItemModel>,
    categoryRankingDate: Pair<LocalDate, LocalDate>,
    onAction: (StatisticsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val joinedDate = LocalDate.of(2023, 5, 10)

    val dateState = rememberStatisticDateState(joinedDate)
    val scrollState = rememberScrollState(0)
    val pullRefreshState = rememberPullToRefreshState()

    LaunchedEffect(dateState.selectedDate) {
        onAction.invoke(StatisticsEvent.SelectDate(dateState.selectedDate))
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MnTheme.backgroundColorScheme.primary,
    ) {
        if (showDatePickerDialog) {
            StatisticDatePickerModal(
                statisticDateState = dateState,
                onDateSelected = { date ->
                    date?.let {
                        dateState.updateDate(it)
                    }
                },
                onDismiss = {
                    onAction.invoke(StatisticsEvent.HideDatePickerDialog)
                },
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            StatisticsTopBar(
                month = targetDate.monthValue.toString(),
                day = targetDate.dayOfMonth.toString(),
                onRightClick = dateState.moveToNextDay,
                onLeftClick = dateState.moveToPreviousDay,
                onMoreClick = { onAction.invoke(StatisticsEvent.ShowDatePickerDialog) },
            )
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxSize(),
                isRefreshing = isLoading,
                state = pullRefreshState,
                onRefresh = {
                    onAction.invoke(StatisticsEvent.Refresh(dateState.selectedDate))
                },
                indicator = {
                    if (isLoading) {
                        MnSpinner(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(MnSpacing.xLarge),
                ) {
                    StatisticsTotalFocusSection(
                        focusTime = focusTime,
                    )
                    StatisticsFocusListSection(
                        focusHistory = focusHistory,
                    )

                    StatisticsGraphSection(
                        targetDate = targetDate,
                        weeklyFocusTime = weeklyFocusTime,
                        weeklyMaxFocusTime = weeklyMaxFocusTime,
                    )
                    CategoryRankingContent(
                        startDate = categoryRankingDate.first,
                        endDate = categoryRankingDate.second,
                        categoryRankingList = categoryRankingList,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticsTotalFocusSection(
    focusTime: Duration,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = MnSpacing.xLarge),
    ) {
        Text(
            text = stringResource(R.string.total_focus_time_title),
            style = MnTheme.typography.header4,
            color = MnTheme.textColorScheme.primary,
            modifier = Modifier.padding(vertical = MnSpacing.xLarge),
        )

        TotalFocusTimeContent(
            focusTime = focusTime,
        )
    }
}

private const val FIRST_PAGE_SIZE = 3
private const val DEFAULT_PAGE_SIZE = 10

@Composable
private fun StatisticsFocusListSection(
    focusHistory: ImmutableList<FocusTimeModel>,
    modifier: Modifier = Modifier,
) {
    var currentHistoryPage by rememberSaveable { mutableIntStateOf(0) }

    val historyPageSize = if (currentHistoryPage == 0) FIRST_PAGE_SIZE else DEFAULT_PAGE_SIZE

    val lastHistoryPage = if (focusHistory.size <= FIRST_PAGE_SIZE) {
        0
    } else {
        (focusHistory.size - FIRST_PAGE_SIZE + DEFAULT_PAGE_SIZE - 1) / DEFAULT_PAGE_SIZE
    }

    if (currentHistoryPage > lastHistoryPage) {
        currentHistoryPage = lastHistoryPage
    }

    Column(
        modifier = modifier
            .padding(horizontal = MnSpacing.xLarge)
            .padding(bottom = MnSpacing.xLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MnSpacing.xLarge),
            horizontalArrangement = Arrangement.spacedBy(MnSpacing.xSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.total_focus_history_title),
                style = MnTheme.typography.header4,
                color = MnTheme.textColorScheme.primary,

            )
            Text(
                text = "${focusHistory.size}",
                style = MnTheme.typography.header4,
                color = MnTheme.backgroundColorScheme.accent1,
            )
        }

        focusHistory.take((currentHistoryPage + 1) * historyPageSize)
            .forEachIndexed { idx, focusData ->
                key(idx) {
                    StatisticsFocusTimeContent(focusData)
                }
            }

        if (currentHistoryPage < lastHistoryPage) {
            MnTextButton(
                text = "더보기",
                onClick = { currentHistoryPage += 1 },
                styles = MnTextButtonStyles.medium,
                rightIconResourceId = R.drawable.ic_chevron_down,
            )
        }
    }
}

@Composable
private fun StatisticsFocusTimeContent(
    focusData: FocusTimeModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        StatisticsContentHeader(
            time = stringResource(
                R.string.common_time_format,
                focusData.totalFocusTime.toHours(),
                focusData.totalFocusTime.toMinutes() % 60,
            ),
        )

        StatisticsContent(
            time = stringResource(
                R.string.common_time_format,
                focusData.totalFocusTime.toHours(),
                focusData.totalFocusTime.toMinutes() % 60,
            ),
            category = focusData.category,
        )
    }
}

@Composable
private fun StatisticsGraphSection(
    targetDate: LocalDate,
    weeklyFocusTime: ImmutableList<Float>,
    weeklyMaxFocusTime: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MnSpacing.xLarge),
    ) {
        Text(
            text = stringResource(R.string.total_focus_graph_title),
            style = MnTheme.typography.header4,
            color = MnTheme.textColorScheme.primary,
        )

        GraphContainer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MnSpacing.xLarge),
            graphData = weeklyFocusTime,
            configure = FocusGraphConfigure(
                maxFocusTime = weeklyMaxFocusTime,
                targetDateTime = LocalDateTime.of(targetDate.year, targetDate.month, targetDate.dayOfMonth, 0, 0),
            ),
        )
    }
}

@ThemePreviews
@Preview
@Composable
private fun StatisticsScreenPreview() {
    MnTheme {
        val previewStatisticsState = StatisticsModel(
            date = LocalDate.of(2024, 4, 7),
            totalFocusTime = Duration.ofMinutes(30),
            focusTimes = (1..7).map {
                FocusTimeModel(
                    no = it,
                    category = PomodoroCategoryModel(
                        categoryNo = it,
                        title = "카테고리 $it",
                        categoryIcon = CategoryIcon.DUMBBELL,
                    ),
                    totalFocusTime = Duration.ofMinutes((10 * it).toLong()),
                )
            },
            weeklyFocusTimeTrend = WeeklyFocusTimeTrendModel(
                startDate = LocalDate.of(2024, 4, 1),
                endDate = LocalDate.of(2024, 4, 7),
                dateToFocusTimeStatistics = (0..6).map { offset ->
                    DailyFocusTimeModel(
                        date = LocalDate.of(2024, 4, 1).plusDays(offset.toLong()),
                        totalFocusTime = Duration.ofMinutes((15 + offset * 5).toLong()),
                    )
                },
            ),
            categoryRanking = CategoryRankingModel(
                startDate = LocalDate.of(2024, 4, 1),
                endDate = LocalDate.of(2024, 4, 7),
                rankingItems = (1..7).map {
                    RankingItemModel(
                        rank = it,
                        category = PomodoroCategoryModel(
                            categoryNo = it,
                            title = "카테고리 $it",
                            categoryIcon = CategoryIcon.CAT,
                        ),
                        totalFocusTime = Duration.ofMinutes((20 * it).toLong()),
                    )
                }.sortedByDescending { it.totalFocusTime }
                    .take(3)
                    .mapIndexed { rank, item -> item.copy(rank = rank + 1) },
            ),
        ).toState()

        StatisticsScreen(
            isLoading = previewStatisticsState.isLoading,
            showDatePickerDialog = false,
            targetDate = LocalDate.of(2024, 4, 3),
            focusTime = previewStatisticsState.totalFocusTime,
            focusHistory = previewStatisticsState.focusTimes,
            weeklyFocusTime = previewStatisticsState.weeklyFocusTimeList,
            weeklyMaxFocusTime = previewStatisticsState.weeklyMaxFocusTime,
            categoryRankingList = previewStatisticsState.categoryRankingList,
            categoryRankingDate = previewStatisticsState.categoryRankingDate,
            onAction = {},
        )
    }
}
