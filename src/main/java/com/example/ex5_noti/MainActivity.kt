package com.example.ex5_noti

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.N
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.example.ex5_noti.databinding.ActivityMainBinding
import com.example.ex5_noti.databinding.DialogCustomBinding
import java.time.MonthDay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //권한 체크
//        val status = ContextCompat.checkSelfPermission(
//            this,
//            "android.permission.ACCESS_FINE_LOCATION"
//        )
//        if (status == PackageManager.PERMISSION_GRANTED) {
//            Log.d("myLog", "권한 허용")
//        } else {
//            Log.d("myLog", "권한 거부")
//        }

        //권한 요청
        val reqPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("myLog", "권한을 허용하였습니다.")
            } else {
                Log.d("myLog", "권한을 거부하였습니다.")
            }
        }
        reqPermissionLauncher.launch("android.permission.ACCESS_FINE_LOCATION")

        // 토스트 메세지 생성
        val toast = Toast.makeText(this, "권한을 허용해주세요.", Toast.LENGTH_SHORT)

        // 버튼 클릭 식 토스트 메세지 발생
        binding.toastBtn.setOnClickListener {
            toast.show()
        }

        // 토스트메세지 콜백 기능
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            toast.addCallback(
                object : Toast.Callback() {
                    override fun onToastHidden() {
                        super.onToastHidden()
                        Log.d("myLog", "토스트가 사라짐")
                    }

                    override fun onToastShown() {
                        super.onToastShown()
                        Log.d("myLog", "토스트가 나타남")
                    }
                }
            )
        }

        //데이트 피커 다이얼로그 사용
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val datePicker =
                DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
                        Log.d("myLog", "${year}년 ${month + 1}월 ${day}일")
                    }
                }, 2022, 9, 1)
            binding.dateBtn.setOnClickListener {
                datePicker.show()
            }

        }

        // 타임피커 다이얼로그 사용
        binding.timeBtn.setOnClickListener {
            TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
                    Log.d("myLog", "선택한 시간은 : ${hour} : ${minute}")
                }
            }, 14, 23, true).show()
        }

        // 알림창 띄우기
        binding.alertBtn.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("테스트 알림")
                setIcon(android.R.drawable.ic_dialog_info)
                setMessage("정말 종료하시겠습니까?")
                setPositiveButton("확인", null)
                setNegativeButton("취소", null)
                setNeutralButton("자세히", null)
                show()
            }
        }

        // 목록을 출력하는 알림창
        val items = arrayOf("사과", "바나나", "배", "키워")

        binding.listBtn.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("다음 중 좋아하는 과일은?")
                setItems(items, object : OnClickListener {
                    override fun onClick(dialog: DialogInterface?, idx: Int) {
                        Log.d("myLog", "선택한 과일 : ${idx}")
                    }

                })
                setPositiveButton("확인", null)
                show()
            }
        }

        //체크박스를 이용한 다중선택
        binding.multiBtn.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("좋아하는 과일을 모두 고르시오.")
                setMultiChoiceItems(items, booleanArrayOf(false, false, false, false),
                    object : DialogInterface.OnMultiChoiceClickListener {
                        override fun onClick(
                            dialog: DialogInterface?,
                            idx: Int,
                            isChecked: Boolean
                        ) {
                            Log.d(
                                "myLog",
                                "${items[idx]}가(이) ${if (isChecked) "선택되었습니다." else "선택이 해제되엇습니다."}"
                            )
                        }
                    })
                setPositiveButton("확인", null)
                show()
            }
        }

        //단일 선택
        //뒤로가기버튼, 화면 바깥 여역 터치시 알림창 닫히지 않도록 설정
        binding.singleBtn.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("좋아하는 과일을 하나만 고르시오.")
                    setSingleChoiceItems(items, 0
                    ) { dialog, idx -> Log.d("myLog", "${items[idx]}가(이) 선택되었습니다.") }
                setPositiveButton("확인", null)
                setCancelable(false) // 뒤로가기 버튼 눌럿을 때 닫히지 않도록 설정
                show()
            }.setCanceledOnTouchOutside(false) // 바깥 여역 터치시 닫히지 않도록 설정
        }

        //커스텀 다이얼로그 출력
        binding.customBtn.setOnClickListener {
            val dialogBinding = DialogCustomBinding.inflate(layoutInflater)
            AlertDialog.Builder(this).run{
                setTitle("커스텀 다이얼로그")
                setView(dialogBinding.root)
                setPositiveButton("확인") { p0, p1 ->
                    if (dialogBinding.jja.isChecked === true) {
                        Log.d("myLog", "짜장면을 선택했습니다.")
                    } else {
                        Log.d("myLog", "짬뽕을 선택했습니다.")
                    }
                }
                show()
            }
        }

        //소리 얻기
        val notification : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(applicationContext, notification)

        binding.soundBtn.setOnClickListener {
            ringtone.play()
        }

        //음원 재생
        val player : MediaPlayer = MediaPlayer.create(this, R.raw.mario)

        binding.soundBtn2.setOnClickListener {
            player.start()
        }


    }
}


