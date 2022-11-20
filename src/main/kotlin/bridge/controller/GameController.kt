package bridge.controller

import bridge.*
import bridge.model.BridgeGame
import bridge.view.InputView
import bridge.view.OutputView

class GameController {

    private val bridgeGame = BridgeGame()
    private var size = ZERO
    private var isPlaying = true

    init {
        OutputView.startGame()
        getBridgeLength()
        println()
        bridgeGame.makeBridge(size)
        bridgeGame.initMatrix(size)
    }

    fun start() {
        while (isPlaying) {
            val direction = getMoveDirection()
            val isSuccess = bridgeGame.compare(direction)
            bridgeGame.move(direction)
            OutputView.printMap(bridgeGame.progressMatrix.progress, bridgeGame.position)
            completeOrFail(isSuccess)
            bridgeGame.position++
        }
    }

    private fun getBridgeLength() {
        while (true) {
            try {
                OutputView.inputLength()
                size = InputView.readBridgeSize()
                break
            } catch (e: Exception) {
                println("$ERROR ${e.message}\n")
            }
        }
    }

    private fun getMoveDirection(): String {
        while (true) {
            try {
                OutputView.selectMove()
                return InputView.readMoving()
            } catch (e: Exception) {
                println("$ERROR ${e.message}\n")
            }
        }
    }

    private fun completeOrFail(isSuccess: Boolean) {
        if (isSuccess) {
            if (bridgeGame.isLastPosition(size - 1)) {
                quit(SUCCESS)
            }
        } else {
            val command = getCommand()
            retryOrQuit(command)
        }
    }

    private fun getCommand(): String {
        while (true) {
            try {
                OutputView.retryOrQuit()
                return InputView.readGameCommand()
            } catch (e: Exception) {
                println("$ERROR ${e.message}\n")
            }
        }
    }

    private fun retryOrQuit(s: String) {
        if (s == QUIT) {
            quit(FAIL)
        } else bridgeGame.retry(size)
    }

    fun quit(resultText: String) {
        OutputView.finalResult()
        OutputView.printResult(bridgeGame.progressMatrix.progress, bridgeGame.position)
        OutputView.isSuccess(resultText)
        OutputView.totalTryCount(bridgeGame.tryCount)
        isPlaying = false
    }
}