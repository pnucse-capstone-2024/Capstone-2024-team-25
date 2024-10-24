/* eslint-disable jsx-a11y/label-has-associated-control */
import { useState } from 'react';

function PBTModal({ onClose, onSubmit, examId }) {
  const [formData, setFormData] = useState({
    examId,
    title: '',
    includeCover: true,
    includeAnswers: true,
    layout: 'double',
  });

  const handleChange = (e) => {
    const { name, value, checked, type } = e.target;
    setFormData((prevFormData) => ({
      ...prevFormData,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
    onClose();
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-gray-600 bg-opacity-50 p-4">
      <form onSubmit={handleSubmit} className="w-full max-w-md space-y-4 rounded-lg bg-white p-8">
        <h2 className="text-2xl font-semibold">Exam Setting</h2>
        <div>
          <label htmlFor="examId" className="block text-sm font-medium text-gray-700">
            Exam ID:
          </label>
          <input
            type="text"
            id="examId"
            name="examId"
            value={formData.examId}
            readOnly
            className="mt-1 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
          />
        </div>
        <div>
          <label htmlFor="title" className="block text-sm font-medium text-gray-700">
            Exam Title:
          </label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="Enter exam title..."
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
          />
        </div>
        <div className="flex items-center">
          <input
            type="checkbox"
            name="includeCover"
            checked={formData.includeCover}
            onChange={handleChange}
            className="h-4 w-4 rounded border-gray-300 text-indigo-600 checked:bg-indigo-600 focus:ring-indigo-500"
          />
          <label className="ml-2 block text-sm text-gray-700">Include Cover</label>
        </div>
        <div className="flex items-center">
          <input
            type="checkbox"
            name="includeAnswers"
            checked={formData.includeAnswers}
            onChange={handleChange}
            className="h-4 w-4 rounded border-gray-300 text-indigo-600 checked:bg-indigo-600 focus:ring-indigo-500"
          />
          <label className="ml-2 block text-sm text-gray-700">Include Answer</label>
        </div>
        <div>
          <span className="block text-sm font-medium text-gray-700">Print Layout:</span>
          <div className="mt-2">
            <label className="mr-6 inline-flex items-center">
              <input
                type="radio"
                name="layout"
                value="single"
                checked={formData.layout === 'single'}
                onChange={handleChange}
                className="form-radio"
              />
              <span className="ml-2">1 Layer(Experimental)</span>
            </label>
            <label className="inline-flex items-center">
              <input
                type="radio"
                name="layout"
                value="double"
                checked={formData.layout === 'double'}
                onChange={handleChange}
                className="form-radio"
              />
              <span className="ml-2">2 Layer(Default)</span>
            </label>
          </div>
        </div>
        <div className="flex justify-end space-x-4">
          <button
            type="submit"
            className="inline-flex justify-center rounded-md border border-transparent bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Print
          </button>
          <button
            type="button"
            onClick={onClose}
            className="inline-flex justify-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}

export default PBTModal;
