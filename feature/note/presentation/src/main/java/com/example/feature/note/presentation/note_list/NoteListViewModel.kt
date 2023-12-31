package com.example.feature.note.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.redux.Container
import com.example.feature.domain.note_list_redux.NoteListAction
import com.example.feature.domain.note_list_redux.NoteListContainer
import com.example.feature.domain.note_list_redux.NoteListSideEffect
import com.example.feature.domain.note_list_redux.NoteListState
import com.example.feature.domain.use_case.NoteUseCases
import com.example.feature.note.data.model.Note
import com.example.feature.note.utils.NoteOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    useCases: NoteUseCases
) : ViewModel() {
    private val container = NoteListContainer(useCases)
    val state: StateFlow<NoteListState> = container.state
    val sideEffect: SharedFlow<NoteListSideEffect> = container.sideEffect

    fun toggleOrderSection() = intent {
        dispatch(NoteListAction.ToggleOrderSection)
    }

    fun fetchOrderedList(order: NoteOrder, initFetch: Boolean = false) = intent {
        if (!initFetch) {
            if (state.value.noteOrder::class == order::class &&
                state.value.noteOrder.orderType == order.orderType
            ) {
                return@intent
            }
        }
        dispatch(NoteListAction.FetchOrderedNoteList(order))
    }

    fun deleteNote(note: Note) = intent {
        dispatch(NoteListAction.DeleteNote(note, state.value.noteOrder))
    }

    fun restoreNote() = intent {
        dispatch(
            NoteListAction.RestoreNote(
                state.value.recentlyDeletedNote ?: return@intent,
                state.value.noteOrder
            )
        )
    }

    fun onNoteClicked(note: Note) = intent {
        dispatch(
            NoteListAction.GoNoteUpdateScreen(
                note.id!!.toInt(),
                note.color
            )
        )
    }

fun gotoAddNoteScreen() = intent {
    dispatch(NoteListAction.GoAddEditScreen)
}

private fun intent(transform: suspend Container<NoteListAction, NoteListState, NoteListSideEffect>.() -> Unit) {
    viewModelScope.launch {
        container.transform()
    }
}
}
