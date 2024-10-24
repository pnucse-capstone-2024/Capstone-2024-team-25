/* eslint-disable no-param-reassign */
/* eslint-disable no-use-before-define */
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';

import font from './HangulFont';

function addNanumGothicFont(doc) {
  doc.addFileToVFS('malgun.ttf', font);
  doc.addFont('malgun.ttf', 'malgun', 'normal');
  doc.setFont('malgun');
}

export default async function createPDF(ref, id, title, isCover, coverProps, isAnswer, answerProps) {
  const element = ref.current;
  const doc = jsPDF('p', 'mm', 'a4', true);
  const pageHeight = 297;
  const pageWidth = pageHeight / 1.414;
  addNanumGothicFont(doc);
  if (isCover && coverProps) {
    const {year, examType, subject, timeLimit, totalQuestions} = coverProps;

    doc.setFontSize(30); // 폰트 크기 설정
    doc.text("TOPIK", 195, 15, null, null, 'right');
    doc.line(10, 20, pageWidth - 10, 20);
    // 제목
    doc.setFontSize(40); // 폰트 크기
    doc.setTextColor('#003366'); // 진한 파란색
    doc.text(`제 ${year}년`, 105, 80, null, null, 'center');
    doc.text('TOPIK', 105, 100, null, null, 'center'); // 중앙 정렬
    doc.text('Actual Test', 105, 120, null, null, 'center');

    // 부제목
    doc.setFontSize(15);
    doc.setTextColor('#333333');
    doc.text(`The ${year} Actual Test of Proficiency in Korean`, 105, 140, null, null, 'center'); // 중앙 정렬
    doc.setFontSize(14);
    doc.text(title, 105, 155, null, null, 'center');
    doc.setFontSize(12);
    doc.text(`ExamID: ${id}`, 105, 170, null, null, 'center');
    
    // 세부 정보
    doc.setFontSize(12);
    doc.setTextColor('#000000');
    let currentHeight = 190;
    const lineHeight = 10;

    doc.line(50, currentHeight-10, pageWidth - 50, currentHeight-10);
    doc.text(`TOPIK ${examType}`, 105, currentHeight, null, null, 'center');
    currentHeight += lineHeight;
    doc.text(subject, 105, currentHeight, null, null, 'center');
    currentHeight += lineHeight;
    doc.text(`Total ${totalQuestions} Questions`, 105, currentHeight, null, null, 'center');
    currentHeight += lineHeight;
    doc.text(`Time Limit: ${timeLimit} minutes`, 105, currentHeight, null, null, 'center');
    doc.line(50, currentHeight+5, pageWidth - 50, currentHeight+5);

    doc.setFontSize(15);
    doc.line(10, pageHeight - 20, pageWidth - 10, pageHeight - 20);
    doc.text("TheGrapeBine", 15, 285, null, null, 'left');
    // doc.text("TheGrapeBine", pageWidth - 15, 285, null, null, 'right');

    doc.addPage();
    // 기존 페이지 추가
  }
  await addMainContent(doc, element, title, id);
  if (isAnswer && answerProps) {
    doc.addPage();
    doc.setFontSize(30); // 폰트 크기 설정
    doc.text("TOPIK", 195, 15, null, null, 'right');
    doc.line(10, 20, 200, 20);
    doc.setFontSize(20);
    doc.text(`Answer Sheet`, 105, 40, null, null, 'center');
    doc.setFontSize(12);
    doc.text(`${title}`, 105, 50, null, null, 'center');
    doc.text(`ExamID: ${id}`, 105, 60, null, null, 'center');
    doc.line(50, 70, 150, 70);
    doc.text("Question", 70, 80, null, null, 'center');
    doc.text("Answer", 130, 80, null, null, 'center');
    doc.line(50, 85, 150, 85);
    doc.setFontSize(10);

    let currentHeight = 90;
    const maxHeight = 270; // 페이지의 최대 높이 설정
    const lineHeight = 10;

    answerProps.forEach((answer, index) => {
        if (currentHeight > maxHeight) {
            doc.addPage();
            currentHeight = 40; // 새 페이지의 시작 높이 설정
            doc.setFontSize(30); // 폰트 크기 설정
            doc.text("TOPIK", 195, 15, null, null, 'right');
            doc.line(10, 20, 200, 20);
            doc.setFontSize(10);
            doc.line(10, pageHeight-20, pageWidth - 10, pageHeight-20);
            if(title!==''){
                doc.text(title, 15, 285, null, null, 'left');
            }else{
            doc.text(id, 15, 285, null, null, 'left');
            }
            doc.text("Answer Sheet", pageWidth - 15, 285, null, null, 'right');
        }
        doc.text(`${index + 1}`, 70, currentHeight, null, null, 'center');
        doc.text(`(${answer.rightAnswer}) 번`, 130, currentHeight, null, null, 'center');
        currentHeight += lineHeight;
    });
}
  window.open(doc.output('bloburl'));
}

async function addMainContent(doc, element, title, id) {
  const canvas = await html2canvas(element, {
    width: element.scrollWidth,
    height: element.clientHeight,
    scale: 2,
    useCORS: true,
    allowTaint: true,
    logging: true,
  });

  const imgData = canvas.toDataURL('image/png');
  const imgHeight = 300;
  const imgWidth = (canvas.width * imgHeight) / canvas.height;
  
  let widthLeft = 0;
  const pageWidth = imgHeight / 1.414;
  
  // 페이지 번호 초기화
  if (doc.pageNumber === undefined) {
    doc.pageNumber = 1; // pageNumber 속성 초기화
  }
  doc.pageNumber = 1;

  doc.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight, undefined, 'FAST', 'NONE');
  addHeaderFooter(doc, title, id, imgHeight);

  widthLeft += pageWidth;

  while (widthLeft < imgWidth) {
    doc.addPage();
    doc.pageNumber += 1;
    doc.addImage(imgData, 'PNG', -widthLeft, 0, imgWidth, imgHeight, undefined, 'FAST', 'NONE');
    addHeaderFooter(doc, title, id, imgHeight);
    widthLeft += pageWidth;
  }
}

function addHeaderFooter(doc, title, id, imgHeight) {
  const pageWidth = imgHeight / 1.414;
  const pageHeight = 297;

  function header() {
    doc.setFontSize(13);
    if (title !== '') {
      doc.text(title, 195, 7, null, null, 'right');
    } else {
      doc.text(id, 195, 7, null, null, 'right');
    }
    doc.line(10, 10, pageWidth - 10, 10);
  }

  function footer() {
    doc.setFontSize(12);
    doc.text(`${doc.pageNumber}`, pageWidth - 15, 292, null, null, 'right'); // print number bottom right
    doc.line(10, pageHeight - 10, pageWidth - 10, pageHeight - 10);
  }

  function injectSplitLine() {
    doc.line(pageWidth / 2, 10, pageWidth / 2, imgHeight - 13);
  }

  header();
  footer();
  injectSplitLine();
}